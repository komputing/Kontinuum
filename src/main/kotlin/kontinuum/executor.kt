package kontinuum

import java.io.File

fun executeAndPrint(vararg commandAndParams: String, outPath: File, workPath: File) {

    try {

        println("executing ${commandAndParams.joinToString(" ")}")
        val directory = ProcessBuilder(commandAndParams.asList()).directory(workPath)
        directory.environment().put("ANDROID_HOME", "/home/ligi/bin/android-sdk/")

        val p = directory.start()

        StreamToFileWriter(p.inputStream, File(outPath, "out.txt")).start()
        StreamToFileWriter(p.errorStream, File(outPath, "err.txt")).start()

        while (p.isAlive) {
            Thread.sleep(100)
        }

        p.errorStream.close()
    } catch(e: Exception) {
        e.printStackTrace()
    }
}
/*

outPath.mkdir()
while (true) {
    File("workspace").listFiles().forEach { projectFile ->

        val repo = FileRepositoryBuilder().findGitDir(projectFile).build()

        Git(repo).fetch().call().messages.forEach {
            println(it)
        }

        val projectPath = File(outPath, projectFile.name)
        projectPath.mkdir()

        repo.tags.forEach { tag ->

            val tagPath = File(projectPath, tag.key)

            if (!tagPath.exists()) {
                tagPath.mkdir()
                println("processing ${projectFile.name} ${tag.key} ")

                executeAndPrint("git", "checkout", tag.key, outPath = tagPath, workPath = projectFile)
                executeAndPrint("./gradlew", "clean", "assembleRelease", outPath = tagPath, workPath = projectFile)

                val apkPath = File(tagPath, "apkpath")
                apkPath.mkdir()
                projectFile.walk().filter { it.name.endsWith(".apk") }.forEach { it.copyTo(File(apkPath, it.name), true) }

                executeAndPrint("./gradlew", "clean", outPath = tagPath, workPath = projectFile)

                println("tag done")
            }

        }


    }

    Thread.sleep(3000)
}
*/