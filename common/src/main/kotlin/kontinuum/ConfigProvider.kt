package kontinuum

import kontinuum.model.config.Config
import okio.buffer
import okio.source

object ConfigProvider {

    val config: Config by lazy {
        configAdapter.fromJson(configFile.source().buffer())!!
    }

}