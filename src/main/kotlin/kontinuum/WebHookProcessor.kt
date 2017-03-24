package kontinuum

import kontinuum.model.github.GithubCommitState
import kontinuum.model.github.GithubCommitStatus


fun processWebHook(event: String, payload: String) {
    when (event) {
        "push" -> {
            val pushInfo = pushEventAdapter.fromJson(payload)
            println("processing push from " + pushInfo.pusher.name + " to " + pushInfo.repository.full_name + " commits:" + pushInfo.commits.size)

            val status = GithubCommitStatus(state = GithubCommitState.success, target_url = "http://ligi.de", description = "yay success from first github integration", context = "kontinuum")
            setStatus(pushInfo.repository.full_name, pushInfo.head_commit.id, status)
        }
    }

}