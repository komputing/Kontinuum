package kontinuum

import kontinuum.ConfigProvider.config
import kotlin.system.exitProcess

fun main() {

    if (!configFile.exists()) {
        println("config not found at $configFile")
        exitProcess(1)
    }

    val versionOfIPFS = ipfs.info.version()

    println("found IPFS service: $versionOfIPFS")

    listOf(workspaceDir, tmpDir, outDir).forEach { it.mkdirs() }

    println("using config: $config")

    startWebServer()
    processWorkPackages()

}

