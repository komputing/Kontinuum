package kontinuum

import kontinuum.ConfigProvider.config
import kontinuum.model.WorkPackage
import kontinuum.model.WorkPackageStatus
import kotlinx.coroutines.*
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
        var initialRun = true
        val initialCommitHashBlacklist = mutableSetOf<String>()
        while (isActive) {
            try {
                val okHttpClient = OkHttpClient.Builder().build()
                val request = Request.Builder().url("https://builder.komputing.org/api")
                okHttpClient.newCall(request.build()).execute().body()?.string()?.let { stringResponse ->

                    val packages = workPackageProviderAdapter.fromJson(stringResponse)

                    if (initialRun) {
                        packages?.forEachIndexed { index, workPackage ->
                            if (index != 0) {
                                initialCommitHashBlacklist.add(workPackage.commitHash)
                            }
                        }
                    }
                    initialRun = false
                    packages?.forEach { newPackage ->
                        if (!initialCommitHashBlacklist.contains(newPackage.commitHash)) {
                            val existing = WorkPackageProvider.packages.firstOrNull { it.commitHash == newPackage.commitHash }
                            if (existing == null) {
                                val epochSeconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
                                val newWorkPackage = WorkPackage(
                                        branch = newPackage.branch,
                                        project = newPackage.project,
                                        commitHash = newPackage.commitHash,
                                        workPackageStatus = WorkPackageStatus.PENDING,
                                        epochSeconds = epochSeconds,
                                        installationId = newPackage.installationId
                                )
                                WorkPackageProvider.packages.add(newWorkPackage)
                            }
                        }
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
            delay(5000)
        }
    }
    processWorkPackages()

}

