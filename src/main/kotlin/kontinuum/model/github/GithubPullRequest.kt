package kontinuum.model.github

data class GithubPullRequest(val url: String,
                             val repo: GithubRepository,
                             val head: GithubCommit?)