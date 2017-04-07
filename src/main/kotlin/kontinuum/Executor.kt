package kontinuum

import java.io.File

fun executeAndPrint(commandAndParams: String, outPath: File, workPath: File) : Int {

    try {
        println("executing $commandAndParams")

        val process = ProcessBuilder(commandAndParams.split(" ")).directory(workPath)

        process.environment().put("ANDROID_HOME", ConfigProvider.config.android_sdk_root)

        val startedProcess = process.start()

        val outFile = File(outPath, "out.txt")
        StreamToFileWriter(startedProcess.inputStream, outFile).start()
        val errorFile = File(outPath, "err.txt")
        StreamToFileWriter(startedProcess.errorStream, errorFile).start()

        while (startedProcess.isAlive) {
            Thread.sleep(100)
        }

        startedProcess.inputStream.close()
        startedProcess.errorStream.close()

        if (errorFile.length()==0L) {
            errorFile.delete()
        }

        if (outFile.length()==0L) {
            outFile.delete()
        }

        return startedProcess.exitValue()
    } catch(e: Exception) {
        e.printStackTrace()
        return 1
    }
}