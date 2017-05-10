package kontinuum.model.github

data class GithubPullRequestEvent(val action: String,
                                  val number: Int,
                                  val pull_request: GithubPullRequest,
                                  val installationInfo: GithubInstallationInfo)