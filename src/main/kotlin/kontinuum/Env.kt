package kontinuum

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.ipfs.kotlin.IPFS
import kontinuum.model.WorkPackage
import kontinuum.model.config.Config
import kontinuum.model.config.RepoConfig
import okhttp3.OkHttpClient
import org.ligi.kithub.model.GithubIssueCommentEvent
import org.ligi.kithub.model.GithubPullRequestEvent
import org.ligi.kithub.model.GithubPushEvent
import java.io.File
import java.util.concurrent.TimeUnit

val configFile = File("kontinuum_config.json")

val workspaceDir = File("workspace")
val tmpDir = File("tmp")
val outDir = File("out")

val okhttp = OkHttpClient.Builder().build()!!
val moshi : Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

val ipfs = IPFS(okHttpClient = OkHttpClient.Builder()
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES).build())

val pushEventAdapter = moshi.adapter(GithubPushEvent::class.java)!!
val issueCommentEventAdapter = moshi.adapter(GithubIssueCommentEvent::class.java)!!
val pullRequestEventAdapter = moshi.adapter(GithubPullRequestEvent::class.java)!!
val configAdapter = moshi.adapter(Config::class.java)!!
val repoConfigAdapter = moshi.adapter(RepoConfig::class.java)!!

val workPackageListType = Types.newParameterizedType(List::class.java, WorkPackage::class.java)
val workPackageProviderAdapter: JsonAdapter<List<WorkPackage>> = moshi.adapter(workPackageListType)!!
