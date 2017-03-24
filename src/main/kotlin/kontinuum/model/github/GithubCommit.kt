package kontinuum.model.github

data class GithubCommit(val id: String,
                        val tree_id: String,
                        val distinct: Boolean,
                        val message: String,
                        val timestamp: String,
                        val url: String,
                        val author: GithubUser,
                        val committer: GithubUser,
                        val added: List<String>,
                        val removed: List<String>,
                        val modified: List<String>)