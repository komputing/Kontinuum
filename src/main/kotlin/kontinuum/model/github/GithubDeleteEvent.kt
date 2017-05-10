package kontinuum.model.github

data class GithubDeleteEvent(val ref: String,
                             val repository: GithubRepository,
                             val installation: GithubInstallationInfo)