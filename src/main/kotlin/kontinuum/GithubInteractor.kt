package kontinuum

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import kontinuum.model.github.GithubCommitStatus
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

fun obtain_private_key(private_key_file: File): PrivateKey {
    val privateKeyBytes = private_key_file.readBytes()
    val encodedKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
    return KeyFactory.getInstance("RSA").generatePrivate(encodedKeySpec)
}

fun setStatus(full_repo: String, commit_id: String, status: GithubCommitStatus) {
    val config = ConfigProvider.config

    val claimsSet = JWTClaimsSet.Builder()
            .issuer(config.github.integration)
            .issueTime(Date())
            .expirationTime(Date(Date().time))
            .build()

    val signer = RSASSASigner(obtain_private_key(File(config.github.cert)))

    val signedJWT = SignedJWT(
            JWSHeader(JWSAlgorithm.RS256),
            claimsSet)

    signedJWT.sign(signer)

    val jwt = signedJWT.serialize()

    val execute = executeCommand(command = "installations/${config.github.installation}/access_tokens", token = jwt, body = RequestBody.create(null, ByteArray(0)))

    val result = tokenResponseAdapter.fromJson(execute.body().source())

    if (execute.code() == 201) {

        val token = result.token
        println("got token:" + token)

        val commitStatusJson = commitStatusAdapter.toJson(status)

        val commitStatus = executeCommand(
                command = "repos/$full_repo/statuses/$commit_id",
                token = token,
                body = RequestBody.create(MediaType.parse("json"), commitStatusJson)
        )

        println("res: " + commitStatus)

    } else {
        println("problem getting token" + execute.code())
    }


}

private fun executeCommand(command: String, token: String, body: RequestBody): Response {
    val request = Request.Builder()
            .post(body)
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/vnd.github.machine-man-preview+json")
            .url("https://api.github.com/$command")
            .build()
    return okhttp.newCall(request).execute()
}

