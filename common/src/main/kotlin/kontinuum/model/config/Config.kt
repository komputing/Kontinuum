package kontinuum.model.config

data class Config(
        val android_sdk_root: String,
        val github: GithubConfig,
        val hasEmulator: Boolean
)