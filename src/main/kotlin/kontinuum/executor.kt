package kontinuum

import java.io.File

fun executeAndPrint(vararg commandAndParams: String, outPath: File, workPath: File) : Int {

    try {

        println("executing ${commandAndParams.joinToString(" ")}")

        val process = ProcessBuilder(commandAndParams.asList()).directory(workPath)

        process.environment().put("ANDROID_HOME", ConfigProvider.config.android_sdk_root)

        val startedProcess = process.start()

        StreamToFileWriter(startedProcess.inputStream, File(outPath, "out.txt")).start()
        StreamToFileWriter(startedProcess.errorStream, File(outPath, "err.txt")).start()

        while (startedProcess.isAlive) {
            Thread.sleep(100)
        }

        startedProcess.inputStream.close()
        startedProcess.errorStream.close()
        return startedProcess.exitValue()
    } catch(e: Exception) {
        e.printStackTrace()
        return 1
    }
}