package kontinuum

import org.jetbrains.ktor.content.readText
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.post
import org.jetbrains.ktor.routing.routing


fun startWebServer() {

    embeddedServer(Netty, 9001) {
        routing {
            post("/") {

                call.request.headers["X-GitHub-Event"]?.let {
                    println("processing github event: $it")
                    val payload = call.request.receiveContent().readText()
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
