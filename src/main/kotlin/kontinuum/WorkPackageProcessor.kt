package kontinuum

import kontinuum.model.StageInfo
import kontinuum.model.StageStatus
import kontinuum.model.WorkPackage
import kontinuum.model.WorkPackageStatus.*
import kontinuum.model.github.GithubCommitState
import kontinuum.model.github.GithubCommitState.*
import kontinuum.model.github.GithubCommitStatus
import okio.Okio
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.JGitInternalException
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.submodule.SubmoduleWalk
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId


fun processWorkPackages() {
    while (true) {

        WorkPackageProvider.packages.firstOrNull { it.workPackageStatus == PENDING }?.let { currentWorkPackage ->

            currentWorkPackage.workPackageStatus = PROCESSING

            println("processing work package: $currentWorkPackage")

            val toPath = java.io.File(workspaceDir, currentWorkPackage.project)

            setStatus(currentWorkPackage, "http://github.com/ligi/kontinuum", pending, "checkout in progress", "checkout")

            val git_res = try {
                val git = if (!toPath.exists()) {
                    Git.cloneRepository()
                            .setURI("https://x-access-token:" + getToken(currentWorkPackage.installationId) + "@github.com/" + currentWorkPackage.project + ".git")
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
                setStatus(currentWorkPackage, hash.hashAsIPFSURL(), error, errorMessage, "checkout")
                false
            }

            if (git_res) {
                val configFile = File(toPath, ".ci/kontinuum.json")
                if (!configFile.exists()) {
                    println("kontinuum config for repo not found")
                } else {
                    val repoConfig = repoConfigAdapter.fromJson(Okio.buffer(Okio.source(configFile)))
                    var hadError = false
                    repoConfig.stages.forEach {
                        if (!hadError) {
                            println("executing stage $it")
                            val epochSeconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
                            val stageInfo = StageInfo(it, StageStatus.PENDING, "", epochSeconds)
                            currentWorkPackage.stageInfoList.add(stageInfo)
                            executeStageByName(it, currentWorkPackage, toPath, stageInfo)
                            stageInfo.endEpochSeconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
                            if (stageInfo.status != StageStatus.SUCCESS) {
                                hadError = true
                            }
                        } else {
                            println("executing stage $it as another failed")
                        }
                    }
                }
            }

            currentWorkPackage.workPackageStatus = FINISHED
        }

        Thread.sleep(1000)
    }

}

fun doIn(name: String, workPackage: WorkPackage, block: (path: File) -> GithubCommitState) {
    println("entering $name")
    setStatus(workPackage, "http://github.com/ligi/kontinuum", pending, "spoon in progress", name)

    val outPath = java.io.File(outDir, workPackage.project + "/" + workPackage.commitHash + "/" + name)
    outPath.mkdirs()

    val result = block.invoke(outPath)

    println("finished $name with $result")
    setStatus(workPackage, addIPFS(outPath), result, "result", name)

}

private fun addIPFS(outPath: File): String {

    val addedContent = ipfs.add.directory(outPath)

    if (addedContent.size == 1) {
        return addedContent.first().Hash.hashAsIPFSURL()
    }

    val direct = addedContent.filter {
        Regex("^file/spoon/.*/debug$").matches(it.Name)
    }

    if (direct.size == 1) {
        return direct.first().Hash.hashAsIPFSURL()
    }

    val joinToString = addedContent.map {
        val name = it.Name.replace("file/", "")
        val url = it.Hash.hashAsIPFSURL()
        "<a href='$url'>$name</a>"
    }.joinToString("<br/>")

    return ipfs.add.string(joinToString).Hash.hashAsIPFSURL()
}

private fun setStatus(currentWorkPackage: WorkPackage, url: String, state: GithubCommitState, description: String, context: String) {
    val githubCommitStatus = GithubCommitStatus(state, target_url = url, description = description, context = "kontinuum/$context")
    setStatus(currentWorkPackage.project, currentWorkPackage.commitHash, githubCommitStatus,currentWorkPackage.installationId)
}