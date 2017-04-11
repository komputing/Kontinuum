package kontinuum

import kontinuum.model.WorkPackage
import kontinuum.model.WorkPackageStatus.PENDING


fun processWebHook(event: String, payload: String) {
    when (event) {
        "push" -> {
            val pushInfo = pushEventAdapter.fromJson(payload)
            println("processing push from " + pushInfo.pusher.name + " to " + pushInfo.repository.full_name + " commits:" + pushInfo.commits.size)

            pushInfo.head_commit?.let {
                WorkPackageProvider.packages.add(WorkPackage(project = pushInfo.repository.full_name, commitHash = it.id, workPackageStatus = PENDING))
            }
        }

        "pull_request" -> {
            val pullRequestInfo = pullRequestEventAdapter.fromJson(payload)
            println("processing pull-request from " + pullRequestEventAdapter)
            //pullRequestInfo.pull_request.user + " to " + pullRequestInfo.pull_request.repo.full_name + " head commit:" + pullRequestInfo.pull_request.head)

            pullRequestInfo.pull_request.head?.let {
                WorkPackageProvider.packages.add(WorkPackage(project = pullRequestInfo.pull_request.repo.full_name, commitHash = it.id, workPackageStatus = PENDING))
            }
        }
    }
}