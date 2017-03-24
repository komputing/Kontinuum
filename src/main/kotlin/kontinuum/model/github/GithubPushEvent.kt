package kontinuum.model.github

data class GithubPushEvent(val ref: String,
                           val commits: List<GithubCommit>,
                           val head_commit: GithubCommit,
                           val compare: String,
                           val pusher: GithubUser,
                           val repository: GithubRepository)