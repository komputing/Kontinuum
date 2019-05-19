package kontinuum

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receiveText
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun startWebServer() {

    embeddedServer(Netty, 9001) {
        routing {
            post("/") {

                call.request.headers["X-GitHub-Event"]?.let {
                    println("processing github event: $it")
                    val payload = call.receiveText()
                    processWebHook(it, payload)
                }
                call.respondText("Thanks GitHub!", ContentType.Text.Plain)
            }

            get("/api") {
                val jsonString = workPackageProviderAdapter.toJson(WorkPackageProvider.getSortedAndCleaned())
                call.respondText(jsonString, ContentType.Application.Json)
            }

            get("/") {
                val html = """<html><head><meta http-equiv="refresh" content="1" /></head><body>""" +
                        WorkPackageProvider.getSortedAndCleaned().joinToString("<hr/>") { pkg ->
                            """<h3>${pkg.project}</h3>${pkg.branch} :${pkg.commitMessage}<br/>""" +
                                    pkg.stageInfoList.joinToString("<br/>") {
                                        """${it.stage}:  ${it.status} <a href="${it.info}">${it.info}</a>"""
                                    }
                        } +
                        "<body></html>"

                call.respondText(html, ContentType.Text.Html)
            }
        }
    }.start(wait = false)
}
