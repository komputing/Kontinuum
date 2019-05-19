import kontinuum.pushEventAdapter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TheGithubPush {


    @Test
    fun shouldBeAbleToParseGithubPushWebhook() {
        val fromJson = pushEventAdapter.fromJson(GIT_PUSH_WEBHOOK_BODY)

        assertThat(fromJson!!.ref).isEqualTo("refs/heads/master")
        assertThat(fromJson.compare).isEqualTo("https://github.com/ligi/GithubCommitStatusTest/compare/a41929413b7e...6c527d796c68")

        assertThat(fromJson.commits.size).isEqualTo(1)

        assertThat(fromJson.commits.first().author.name).isEqualTo("ligi")

    }
}