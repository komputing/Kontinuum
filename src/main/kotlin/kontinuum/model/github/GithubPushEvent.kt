package kontinuum.model.github

data class GithubPushEvent(val ref: String,
                           val created: Boolean,
                           val deleted: Boolean,
                           val forced: Boolean,
                           val commits: List<GithubCommit>,
                           val head_commit: GithubCommit?,
                           val compare: String,
                           val pusher: GithubUser,
                           val repository: GithubRepository,
                           val installation: GithubInstallationInfo)