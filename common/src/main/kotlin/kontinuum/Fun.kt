package kontinuum

import java.io.PrintWriter
import java.io.StringWriter

fun String?.hashAsIPFSGatewayURL() = "https://gateway.ipfs.io/ipfs/$this"

fun Throwable.getStacktraceAsString(): String {
    val errors = StringWriter()
    this.printStackTrace(PrintWriter(errors))
    return errors.toString()
}