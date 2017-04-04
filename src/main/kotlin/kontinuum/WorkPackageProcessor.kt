package kontinuum

import kontinuum.model.WorkPackage
import kontinuum.model.github.GithubCommitState
import kontinuum.model.github.GithubCommitState.*
import kontinuum.model.github.GithubCommitStatus
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.JGitInternalException
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File




fun processWorkPackages() {
    while (true) {

        if (!WorkPackageProvider.packages.isEmpty()) {

            val currentWorkPackage = WorkPackageProvider.packages.removeAt(0)

            println("processing work package: $currentWorkPackage")

            val toPath = java.io.File(workspaceDir, currentWorkPackage.project)

            setStatus(currentWorkPackage, "http://github.com/ligi/kontinuum", pending, "checkout in progress", "checkout")

            val git_res=try {
                val git = if (!toPath.exists()) {
                    Git.cloneRepository()
                            .setURI("https://x-access-token:" + getToken() + "@github.com/" + currentWorkPackage.project + ".git")
                            .setCloneSubmodules(true)
                            .setDirectory(toPath)
                            .call()
                } else {

                    Git(FileRepositoryBuilder().findGitDir(toPath).build()).apply {
                        fetch().call()
                    }
                }

                git.checkout().setName(currentWorkPackage.commitHash).call()
                git.submoduleUpdate().call()
                setStatus(currentWorkPackage, "http://github.com/ligi/kontinuum", success, "checkout done", "checkout")

                println("processing commit: " + git.log().setMaxCount(1).call().first().fullMessage)
                true
            } catch (e:JGitInternalException) {
                val errorMessage = "error while checkout: " + e.message

                val hash = ipfs.add.string(e.getStacktraceAsStting()).Hash
                setStatus(currentWorkPackage, hash.hashAsIPFSURL(), error, errorMessage, "checkout")
                false
            }

            if (git_res) {
                doIn("spoon", currentWorkPackage, { outPath ->
                    val spoonResult = executeAndPrint("./gradlew", "clean", "spoon", "-PsingleFlavor", workPath = toPath, outPath = outPath)
                    println("finished spoon with " + spoonResult)
                    toPath.walk().filter { it.name == "spoon" }.forEach { it.copyRecursively(File(outPath, it.name), true) }

                    if (spoonResult == 0) success else error
                })

                doIn("lint", currentWorkPackage, { outPath ->

                    val result = executeAndPrint("./gradlew", "lint", "-PsingleFlavor", workPath = toPath, outPath = outPath)

                    if (result == 0) {
                        toPath.walk().filter { it.name.startsWith("lint-results") }.forEach { it.copyTo(File(outPath, it.name), true) }
                        success
                    } else {
                        error
                    }
                })

                doIn("test", currentWorkPackage, { outPath ->

                    val result = executeAndPrint("./gradlew", "test", "-PsingleFlavor", workPath = toPath, outPath = outPath)

                    if (result == 0) {
                        toPath.walk().filter { it.name == "tests" }.forEach { it.copyTo(File(outPath, it.name), true) }
                        success
                    } else {
                        error
                    }
                })

                doIn("assemble", currentWorkPackage, { outPath ->

                    val result = executeAndPrint("./gradlew", "assembleRelease", workPath = toPath, outPath = outPath)

                    if (result == 0) {
                        toPath.walk().filter { it.name.endsWith(".apk") }.forEach { it.copyTo(File(outPath, it.name), true) }
                        success
                    } else {
                        error
                    }
                })
            }
        }
        Thread.sleep(1000)
    }

}

fun doIn(name: String, workPackage: WorkPackage, block: (path: File) -> GithubCommitState) {
    println("entering $name")
    setStatus(workPackage, "http://github.com/ligi/kontinuum", pending, "spoon in progress",name)

    val outPath = java.io.File(outDir, workPackage.project + "/" + workPackage.commitHash + "/" + name)
    outPath.mkdirs()

    val result = block.invoke(outPath)

    println("finished $name with $result")
    setStatus(workPackage, addIPFS(outPath), result, "result", name)

}

private fun addIPFS(outPath: File): String {
    ipfs.add.directory(outPath).forEach {
        println("ipfs file: " + it)
    }

    val find = ipfs.add.directory(outPath).find { it.Name == "file" }
    return find?.Hash.hashAsIPFSURL()
}

private fun setStatus(currentWorkPackage: WorkPackage, url: String, state: GithubCommitState, description: String, context: String) {
    val githubCommitStatus = GithubCommitStatus(state, target_url = url, description = description, context = "kontinuum/$context")
    setStatus(currentWorkPackage.project, currentWorkPackage.commitHash, githubCommitStatus)
}