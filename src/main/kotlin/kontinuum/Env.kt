package kontinuum

import com.squareup.moshi.Moshi
import kontinuum.model.config.Config
import kontinuum.model.github.GithubCommitStatus
import kontinuum.model.github.GithubPushEvent
import kontinuum.model.github.TokenResponse
import okhttp3.OkHttpClient
import java.io.File

val configFile = File("kontinuum_config.json")

val okhttp = OkHttpClient.Builder().build()
val moshi = Moshi.Builder().build()

val tokenResponseAdapter = moshi.adapter(TokenResponse::class.java)
val commitStatusAdapter = moshi.adapter(GithubCommitStatus::class.java)
val pushEventAdapter = moshi.adapter(GithubPushEvent::class.java)
val configAdapter = moshi.adapter(Config::class.java)

