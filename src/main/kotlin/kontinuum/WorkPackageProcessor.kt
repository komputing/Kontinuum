package kontinuum

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder


fun processWorkPackages() {
    while (true) {

        if (!WorkPackageProvider.packages.isEmpty()) {

            val currentWorkPackage = WorkPackageProvider.packages.removeAt(0)

            println("processing work package: $currentWorkPackage")

            val toPath = java.io.File(workspaceDir, currentWorkPackage.project)
            val outPath = java.io.File(outDir, currentWorkPackage.project + "/" + currentWorkPackage.commitHash)
            outPath.mkdirs()

            val git = if (!toPath.exists()) {
                Git.cloneRepository()
                        .setURI("https://x-access-token:" + getToken() + "@github.com/" + currentWorkPackage.project + ".git")
                        .setDirectory(toPath)
                        .call()
            } else {

                Git(FileRepositoryBuilder().findGitDir(toPath).build()).apply {
                    fetch().call()
                }
            }

            git.checkout().setName(currentWorkPackage.commitHash).call()
            println("processing commit: " + git.log().setMaxCount(1).call().first().fullMessage)

            executeAndPrint("./gradlew", "clean", "assembleRelease", workPath = toPath, outPath = outPath)

            /*
            val apkPath = File(tagPath, "apkpath")
            apkPath.mkdir()
            projectFile.walk().filter { it.name.endsWith(".apk") }.forEach { it.copyTo(File(apkPath, it.name), true) }
            */

            println("finished building")

        } else {
            println("no work")
        }
        Thread.sleep(1000)
    }

}