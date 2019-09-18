package kontinuum

import java.io.PrintWriter
import java.io.StringWriter

// TODO investigate fs:/ links - as far as I see they should be used instead of ipfs:// - but it did not work with IPFSCompanion
fun String?.hashAsIPFSURL() = "ipfs://$this"

fun String?.hashAsIPFSGatewayURL() = "https://gateway.ipfs.io/ipfs/$this"

fun Throwable.getStacktraceAsString(): String {
    val errors = StringWriter()
    this.printStackTrace(PrintWriter(errors))
    return errors.toString()
}