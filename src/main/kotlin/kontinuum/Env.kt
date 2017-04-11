package kontinuum

import com.squareup.moshi.Moshi
import io.ipfs.kotlin.IPFS
import kontinuum.model.config.Config
import kontinuum.model.config.RepoConfig
import kontinuum.model.github.GithubCommitStatus
import kontinuum.model.github.GithubPullRequestEvent
import kontinuum.model.github.GithubPushEvent
import kontinuum.model.github.TokenResponse
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
val pullRequestEventAdapter = moshi.adapter(GithubPullRequestEvent::class.java)!!
val configAdapter = moshi.adapter(Config::class.java)!!
val repoConfigAdapter = moshi.adapter(RepoConfig::class.java)!!
