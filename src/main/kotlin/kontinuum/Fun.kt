package kontinuum

import java.io.PrintWriter
import java.io.StringWriter

// at some point in the future we can hopefully use fs:/ipfs
fun String?.hashAsIPFSURL() = "https://gateway.ipfs.io/ipfs/$this"

fun Throwable.getStacktraceAsStting(): String {
    val errors = StringWriter()
    this.printStackTrace(PrintWriter(errors))
    return errors.toString()
}