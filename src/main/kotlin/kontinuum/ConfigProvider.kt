package kontinuum

import okio.Okio
import java.io.File

object ConfigProvider {

    val config by lazy {
        configAdapter.fromJson(Okio.buffer(Okio.source(File("kontinuum_config.json"))))
    }

}