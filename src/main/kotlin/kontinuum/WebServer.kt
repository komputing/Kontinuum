package kontinuum

import kontinuum.model.WorkPackage
import kontinuum.model.WorkPackageStatus.*
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
                    processWebHook(it, payload)
                }
                call.respondText("Thanks GitHub!", ContentType.Text.Plain)
            }

            get("/") {
                var res = "<html><head><title>Kontinuum Progress</title><meta http-equiv=\"refresh\" content=\"2\"></head><body>"

                res += "<h1>Pending WorkPackages</h1>"
                res += WorkPackageProvider.packages.filter { it.workPackageStatus == PENDING }.toHTML()

                res += "<h1>WorkPackages in progress</h1>"
                res += WorkPackageProvider.packages.filter { it.workPackageStatus == PROCESSING }.toHTML()

                res += "<h1>Finished WorkPackages</h1>"
                res += WorkPackageProvider.packages.filter { it.workPackageStatus == FINISHED }.toHTML()

                res += "</body></html>"
                call.respondText(res, ContentType.Text.Html)
            }
        }
    }.start(wait = false)


}

fun List<WorkPackage>.toHTML() = map {
    it.commitHash + "@" + it.project + "<br/>" + it.stageInfoList.map { it.stage + " " + it.status }.joinToString("<br/>")
}.joinToString("<hr/>")
