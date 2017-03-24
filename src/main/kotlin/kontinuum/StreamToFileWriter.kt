package kontinuum

import okio.Okio
import java.io.*

class StreamToFileWriter(private val inputStream: InputStream, private val file: File) : Thread() {

    override fun run() {

        val buffer = Okio.buffer(Okio.source(inputStream))
        val outBuffer = Okio.buffer(Okio.sink(file))

        while (!buffer.exhausted()) {
            outBuffer.writeAll(buffer)
        }

        outBuffer.close()
        buffer.close()

    }
}
