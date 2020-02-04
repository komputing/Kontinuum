package kontinuum

import kontinuum.ConfigProvider.config
import kontinuum.model.StageInfo
import kontinuum.model.StageStatus
import kontinuum.model.WorkPackage
import kontinuum.model.WorkPackageStatus.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.buffer
import okio.source
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.JGitInternalException
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.submodule.SubmoduleWalk
import org.ligi.kithub.GithubApplicationAPI
import org.ligi.kithub.model.GithubCommitState
import org.ligi.kithub.model.GithubCommitState.*
import org.ligi.kithub.model.GithubCommitStatus
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId

val githubInteractor by lazy {
    GithubApplicationAPI(
            integration = config.github.integration,
            cert = File(basePath, config.github.cert),
            moshi = moshi
    )
}

fun processWorkPackages() {

    while (true) {
        val packageToProcess = WorkPackageProvider.packages.firstOrNull { it.workPackageStatus == PENDING }


        if (packageToProcess != null)
            try {
                processWorkPackage(packageToProcess)
            } catch (e: Exception) {
                println("problem in work package $packageToProcess")
                e.printStackTrace()
            }

        Thread.sleep(1000)
    }

}

private fun processWorkPackage(currentWorkPackage: WorkPackage) = GlobalScope.launch {

    currentWorkPackage.workPackageStatus = PROCESSING

    println("processing work package: $currentWorkPackage")

    val toPath = File(workspaceDir, currentWorkPackage.project)

    setStatus(currentWorkPackage, "http://github.com/ligi/kontinuum", pending, "checkout in progress", "checkout")

    val git_res = try {
        val git = if (!toPath.exists()) {
            Git.cloneRepository()
                    .setURI("https://x-access-token:" + githubInteractor.getToken(currentWorkPackage.installationId) + "@github.com/" + currentWorkPackage.project + ".git")
                    .setCloneSubmodules(true)

                    .setDirectory(toPath)
                    .call()
        } else {

            Git(FileRepositoryBuilder().findGitDir(toPath).build()).apply {
                fetch().call()
            }
        }

        git.checkout().setName(currentWorkPackage.commitHash).call()

        val walk = SubmoduleWalk.forIndex(git.repository)
        while (walk.next()) {
            val submodule = walk.repository
            Git.wrap(submodule).fetch().call()
            submodule.close()
        }

        git.submoduleUpdate().call()

        currentWorkPackage.commitMessage = git.log().setMaxCount(1).call().first().fullMessage
        println("processing commit: " + currentWorkPackage.commitMessage)

        setStatus(currentWorkPackage, "http://github.com/ligi/kontinuum", success, "checkout done", "checkout")

        println("cleaning: ")
        executeAndPrint("./gradlew clean", toPath)
        true
    } catch (e: JGitInternalException) {
        val errorMessage = "error while checkout: " + e.message

        val hash = ipfs.add.string(e.getStacktraceAsString()).Hash
        setStatus(currentWorkPackage, hash.hashAsIPFSGatewayURL(), error, errorMessage, "checkout")
        false
    }

    if (git_res) {
        val configFile = File(toPath, ".ci/kontinuum.json")
        if (!configFile.exists()) {
            println("kontinuum config for repo not found")
        } else {
            val repoConfig = repoConfigAdapter.fromJson(configFile.source().buffer())
            var hadError = false
            repoConfig?.stages?.forEach {
                when {
                    hadError -> println("not executing stage $it as another failed")
                    it.needsEmulator && !config.hasEmulator -> println("skipping stage as emulator needed but not present")
                    else -> {
                        println("executing stage $it")
                        val epochSeconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
                        val stageInfo = StageInfo(it.name, StageStatus.PENDING, "", epochSeconds)
                        currentWorkPackage.stageInfoList.add(stageInfo)
                        executeStageByName(it.name, currentWorkPackage, toPath, stageInfo)
                        while (stageInfo.status == StageStatus.PENDING) {
                            delay(50)
                        }
                        stageInfo.endEpochSeconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()

                        if (stageInfo.status != StageStatus.SUCCESS) {
                            hadError = true
                        }
                    }
                }
            }
        }
    }

    currentWorkPackage.workPackageStatus = FINISHED
}


fun doIn(stageInfo: StageInfo, workPackage: WorkPackage, block: (path: File) -> GithubCommitState) = GlobalScope.launch {
    println("entering ${stageInfo.stage}")
    setStatus(workPackage, "http://github.com/ligi/kontinuum", pending, "in progress", stageInfo.stage)

    val outPath = File(outDir, workPackage.project + "/" + workPackage.commitHash + "/" + stageInfo.stage)
    outPath.mkdirs()

    val result = block.invoke(outPath)

    println("finished ${stageInfo.stage} with $result")
    val url = addIPFS(outPath)
    stageInfo.info = url
    setStatus(workPackage, url, result, "result", stageInfo.stage)

}

private fun addIPFS(outPath: File): String {

    val addedContent = ipfs.add.directory(outPath)

    if (addedContent.size == 1) {
        return addedContent.first().Hash.hashAsIPFSGatewayURL()
    }

    val direct = addedContent.filter {
        Regex("^file/spoon/.*/debug$").matches(it.Name)
    }

    if (direct.size == 1) {
        return direct.first().Hash.hashAsIPFSGatewayURL()
    }

    val joinToString = addedContent.joinToString("<br/>") {
        val name = it.Name.replace("file/", "")
        val url = it.Hash.hashAsIPFSGatewayURL()
        "<a href='$url'>$name</a>"
    }

    return ipfs.add.string(joinToString).Hash.hashAsIPFSGatewayURL()
}

private suspend fun setStatus(currentWorkPackage: WorkPackage, url: String, state: GithubCommitState, description: String, context: String) {
    val githubCommitStatus = GithubCommitStatus(state, target_url = url, description = description, context = "kontinuum/$context")
    githubInteractor.setStatus(currentWorkPackage.project, currentWorkPackage.commitHash, githubCommitStatus, currentWorkPackage.installationId)
}