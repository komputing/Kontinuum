package kontinuum

import kontinuum.ConfigProvider.config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.System.exit

suspend fun main() {

    if (!configFile.exists()) {
        println("config not found at $configFile")
        exit(1)
    }

    val versionOfIPFS = ipfs.info.version()

    println("found IPFS service: $versionOfIPFS")

    listOf(workspaceDir, tmpDir, outDir).forEach { it.mkdirs() }

    println("using config: $config")


    GlobalScope.launch(Dispatchers.Default) {
        while (true) {
            val okHttpClient = OkHttpClient.Builder().build()
            val res= okHttpClient.newCall(Request.Builder().url("http://builder.komputing.org/api").build()).execute().body()?.string()
            println("." + res)
            delay(5000)
        }
    }
    processWorkPackages()

}

