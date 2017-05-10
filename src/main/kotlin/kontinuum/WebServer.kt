package kontinuum

import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.netty.embeddedNettyServer
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.post
import org.jetbrains.ktor.routing.routing


fun startWebServer() {

    embeddedNettyServer(9001) {
        routing {
            post("/") {

                call.request.headers["X-GitHub-Event"]?.let {
                    println("processing github event: $it")
                    val payload = call.request.content[String::class]
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
                            """<h3>${it.project}</h3>
                            ${it.branch} :${it.commitMessage}<br/>
                            """ + it.stageInfoList.map {
                                """
                                ${it.stage}:  ${it.status} <a href="${it.info}">${it.info}</a>
                                """
                            }.joinToString("<br/>")
                        }.joinToString("<hr/>") +
                        "<body></html>"

                call.respondText(html, ContentType.Text.Html)
            }
        }
    }.start(wait = false)
}
