package kontinuum

import kontinuum.model.WorkPackage
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
                    println("processing github event: " + it)
                    val payload = call.request.content[String::class]
                    processWebHook(it,payload)
                }
                call.respondText("Thanks GitHub!", ContentType.Text.Plain)
            }

            get("/") {
                WorkPackageProvider.packages.add(WorkPackage("ligi/passandroid","dc9bbdb11bef46e1437a7b99fe2f187dee8d92f2"))
                call.respondText("Thanks GitHub!", ContentType.Text.Plain)
            }
        }
    }.start(wait = false)
}