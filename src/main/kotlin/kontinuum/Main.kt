package kontinuum

import java.lang.System.exit

fun main(args: Array<String>) {

    if (!configFile.exists()) {
        println("config not found at $configFile")
        exit(1)
    }

    listOf(workspaceDir, tmpDir, outDir).forEach { it.mkdirs() }

    println("using config: " + ConfigProvider.config)
    startWebServer()
    processWorkPackages()

}

