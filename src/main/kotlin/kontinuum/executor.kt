package kontinuum

import java.io.File

fun executeAndPrint(vararg commandAndParams: String, outPath: File, workPath: File) {

    try {

        println("executing ${commandAndParams.joinToString(" ")}")
        val directory = ProcessBuilder(commandAndParams.asList()).directory(workPath)
        directory.environment().put("ANDROID_HOME", ConfigProvider.config.android_sdk_root)

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