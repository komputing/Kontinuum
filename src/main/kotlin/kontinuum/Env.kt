package kontinuum

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.ipfs.kotlin.IPFS
import kontinuum.model.WorkPackage
import kontinuum.model.config.Config
import kontinuum.model.config.RepoConfig
import kontinuum.model.github.*
import okhttp3.OkHttpClient
import java.io.File

val configFile = File("kontinuum_config.json")

val workspaceDir = File("workspace")
val tmpDir = File("tmp")
val outDir = File("out")

val okhttp = OkHttpClient.Builder().build()!!
val moshi = Moshi.Builder().build()!!

val ipfs = IPFS()

val tokenResponseAdapter = moshi.adapter(TokenResponse::class.java)!!
val commitStatusAdapter = moshi.adapter(GithubCommitStatus::class.java)!!
val pushEventAdapter = moshi.adapter(GithubPushEvent::class.java)!!
val deleteEventAdapter = moshi.adapter(GithubDeleteEvent::class.java)!!
val pullRequestEventAdapter = moshi.adapter(GithubPullRequestEvent::class.java)!!
val configAdapter = moshi.adapter(Config::class.java)!!
val repoConfigAdapter = moshi.adapter(RepoConfig::class.java)!!

val workPackageListType = Types.newParameterizedType(List::class.java,WorkPackage::class.java)
val workPackageProviderAdapter: JsonAdapter<List<WorkPackage>> = moshi.adapter(workPackageListType)!!

