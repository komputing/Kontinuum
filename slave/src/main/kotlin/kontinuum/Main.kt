package kontinuum

import kontinuum.ConfigProvider.config
import kontinuum.model.WorkPackage
import kontinuum.model.WorkPackageStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.system.exitProcess

suspend fun main() {

    if (!configFile.exists()) {
        println("config not found at $configFile")
        exitProcess(1)
    }

    val versionOfIPFS = ipfs.info.version()

    println("found IPFS service: $versionOfIPFS")

    listOf(workspaceDir, tmpDir, outDir).forEach { it.mkdirs() }

    println("using config: $config")

    startWebServer()

    GlobalScope.launch(Dispatchers.Default) {
        while (true) {
            val okHttpClient = OkHttpClient.Builder().build()
            val res = okHttpClient.newCall(Request.Builder().url("http://builder.komputing.org/api").build()).execute().body()?.string()

            val packages = workPackageProviderAdapter.fromJson(res)

            packages?.forEach { newPackage ->
                val exisiting = WorkPackageProvider.packages.firstOrNull { it.commitHash == newPackage.commitHash }
                if (exisiting == null) {
                    val epochSeconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
                    WorkPackageProvider.packages.add(WorkPackage(
                            branch = newPackage.branch,
                            project = newPackage.project,
                            commitHash = newPackage.commitHash,
                            workPackageStatus = WorkPackageStatus.PENDING,
                            epochSeconds = epochSeconds,
                            installationId = newPackage.installationId
                    )

                    )
                }
            }
            delay(5000)
        }
    }
    processWorkPackages()

}

