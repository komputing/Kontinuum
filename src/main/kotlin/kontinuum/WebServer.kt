package kontinuum

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.document
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
                    val payload = call.request.document().reader().readText()
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
                        WorkPackageProvider.getSortedAndCleaned().map {
                            """<h3>${it.project}</h3>${it.branch} :${it.commitMessage}<br/>""" +
                                    it.stageInfoList.joinToString("<br/>") {
                                        """${it.stage}:  ${it.status} <a href="${it.info}">${it.info}</a>"""
                                    }
                        }.joinToString("<hr/>") +
                        "<body></html>"

                call.respondText(html, ContentType.Text.Html)
            }
        }
    }.start(wait = false)
}
