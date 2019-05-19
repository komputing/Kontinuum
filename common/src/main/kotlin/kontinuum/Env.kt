package kontinuum

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.ipfs.kotlin.IPFS
import io.ipfs.kotlin.IPFSConfiguration
import kontinuum.model.WorkPackage
import kontinuum.model.config.Config
import kontinuum.model.config.RepoConfig
import okhttp3.OkHttpClient
import org.ligi.kithub.model.GithubIssueCommentEvent
import org.ligi.kithub.model.GithubPullRequestEvent
import org.ligi.kithub.model.GithubPushEvent
import java.io.File
import java.util.concurrent.TimeUnit

val basePath = File("..")
val configFile = File(basePath, "/kontinuum_config.json")

val workspaceDir by lazy {
    File(ConfigProvider.config.workspace_root).apply {
        mkdirs()
    }
}
val tmpDir = File("tmp")
val outDir = File("out")

val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

val ipfs = IPFS(IPFSConfiguration(okHttpClient = createOKHtttpClient()))

val pushEventAdapter = moshi.adapter(GithubPushEvent::class.java)!!

private fun createOKHtttpClient() = OkHttpClient.Builder()
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES).build()


val issueCommentEventAdapter = moshi.adapter(GithubIssueCommentEvent::class.java)!!
val pullRequestEventAdapter = moshi.adapter(GithubPullRequestEvent::class.java)!!
val configAdapter = moshi.adapter(Config::class.java)!!
val repoConfigAdapter = moshi.adapter(RepoConfig::class.java)!!

val workPackageListType = Types.newParameterizedType(List::class.java, WorkPackage::class.java)
val workPackageProviderAdapter: JsonAdapter<List<WorkPackage>> = moshi.adapter(workPackageListType)!!
