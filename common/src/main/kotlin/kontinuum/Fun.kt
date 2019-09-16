package kontinuum

import java.io.PrintWriter
import java.io.StringWriter

// at some point in the future we can hopefully use fs:/ipfs
fun String?.hashAsIPFSURL() = "fs:/ipfs/$this"

fun Throwable.getStacktraceAsString(): String {
    val errors = StringWriter()
    this.printStackTrace(PrintWriter(errors))
    return errors.toString()
}