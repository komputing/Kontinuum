package kontinuum

import kontinuum.ConfigProvider.config
import java.lang.System.exit

fun main() {

    if (!configFile.exists()) {
        println("config not found at $configFile")
        exit(1)
    }

    val versionOfIPFS = ipfs.info.version()

    println("found IPFS service: $versionOfIPFS")

    listOf(workspaceDir, tmpDir, outDir).forEach { it.mkdirs() }

    println("using config: $config")

    startWebServer()
    processWorkPackages()

}

