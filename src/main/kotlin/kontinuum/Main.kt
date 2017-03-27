package kontinuum

import kontinuum.ConfigProvider.config
import java.lang.System.exit
import java.net.ConnectException

fun main(args: Array<String>) {

    if (!configFile.exists()) {
        println("config not found at $configFile")
        exit(1)
    }

    val IPFSVersion = try {
        ipfs.info.version()
    } catch (e: ConnectException) {
        println("cannot connect to IPFS " + e.message)
        println("trying to start IPFS daemon in path " + config.ipfs_path)

        ProcessBuilder(config.ipfs_path + "/ipfs", "daemon").start()
    }

    println("found IPFS service: " + IPFSVersion)

    listOf(workspaceDir, tmpDir, outDir).forEach { it.mkdirs() }

    println("using config: " + config)

    startWebServer()
    processWorkPackages()

}

