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
import java.io.File
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

val config = ConfigProvider.config

fun obtain_private_key(private_key_file: File): PrivateKey {
    val privateKeyBytes = private_key_file.readBytes()
    val encodedKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
    return KeyFactory.getInstance("RSA").generatePrivate(encodedKeySpec)
}

fun getToken(installation: String): String? {

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

    val execute = executeCommand(command = "installations/${installation}/access_tokens", token = jwt, body = RequestBody.create(null, ByteArray(0)))

    if (execute == null) {
        return null
    }
    return tokenResponseAdapter.fromJson(execute).token

}

fun setStatus(full_repo: String, commit_id: String, status: GithubCommitStatus, installation: String) {

    val token = getToken(installation)

    val commitStatusJson = commitStatusAdapter.toJson(status)

    executeCommand(
            command = "repos/$full_repo/statuses/$commit_id",
            token = token!!,
            body = RequestBody.create(MediaType.parse("json"), commitStatusJson)
    )

}

private fun executeCommand(command: String, token: String, body: RequestBody): String? {
    val request = Request.Builder()
            .post(body)
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/vnd.github.machine-man-preview+json")
            .url("https://api.github.com/$command")
            .build()

    val execute = okhttp.newCall(request).execute()
    val res = execute.body().string()
    execute.body().close()

    if (execute.code() / 100 != 2) {
        println("problem executing $command $res")
        return null
    }


    return res
}

