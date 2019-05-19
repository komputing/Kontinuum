package kontinuum

import kontinuum.model.config.Config
import okio.Okio

object ConfigProvider {

    val config: Config by lazy {
        configAdapter.fromJson(Okio.buffer(Okio.source(configFile)))!!
    }

}