package kontinuum

import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.InputStream

class StreamToFileWriter(private val inputStream: InputStream, private val file: File) : Thread() {

    override fun run() {

        val buffer = inputStream.source().buffer()
        val outBuffer = file.sink().buffer()

        while (!buffer.exhausted()) {
            outBuffer.writeAll(buffer)
        }

        outBuffer.close()
        buffer.close()

    }
}
