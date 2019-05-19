package kontinuum

import kontinuum.model.WorkPackage
import kontinuum.model.WorkPackageStatus.PENDING
import java.time.LocalDateTime
import java.time.ZoneId


fun processWebHook(event: String, payload: String) {
    val epochSeconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()

    when (event) {
        "issue_comment" -> {
            val commit_info = issueCommentEventAdapter.fromJson(payload)
            if (commit_info != null) {
                val pullRequest = commit_info.issue.pull_request

                if (pullRequest != null) {
                    println("was on pull-request " + pullRequest.url)
                    if (commit_info.sender.login == "ligi" && commit_info.comment.body.contains(":microscope:")) {
                        println("was from ligi with microscope")
                    }
                }
            }
        }
        "push" -> {
            val pushInfo = pushEventAdapter.fromJson(payload)
            if (pushInfo != null) {
                println("processing push from " + pushInfo.pusher.name + " to " + pushInfo.repository.full_name + " commits:" + pushInfo.commits.size)

                pushInfo.head_commit?.let {
                    val branch = pushInfo.ref.split("/").last()
                    val workPackage = WorkPackage(
                            branch = branch,
                            project = pushInfo.repository.full_name,
                            commitHash = it.id,
                            workPackageStatus = PENDING,
                            epochSeconds = epochSeconds,
                            installationId = pushInfo.installation.id
                    )

                    WorkPackageProvider.packages.add(workPackage)
                }
            }
        }

        "delete" -> {
            val deleteInfo = pushEventAdapter.fromJson(payload)
            if (deleteInfo != null) {
                WorkPackageProvider.packages.removeIf {
                    it.branch == deleteInfo.ref && it.project == deleteInfo.repository.full_name
                }
            }
        }

        "pull_request" -> {
            val pullRequestInfo = pullRequestEventAdapter.fromJson(payload)
            println("processing pull-request from " + pullRequestEventAdapter)
            //pullRequestInfo.pull_request.user + " to " + pullRequestInfo.pull_request.repo.full_name + " head commit:" + pullRequestInfo.pull_request.head)

            pullRequestInfo?.pull_request?.head?.let {
                val repo = pullRequestInfo.pull_request.repo
                if (repo == null) {
                    println("Repo for PR is null")
                } else {
                    val workPackage = WorkPackage(
                            project = repo.full_name,
                            commitHash = it.id,
                            workPackageStatus = PENDING,
                            epochSeconds = epochSeconds,
                            installationId = pullRequestInfo.installationInfo.id

                    )
                    WorkPackageProvider.packages.add(workPackage)
                }
            }
        }
    }
}