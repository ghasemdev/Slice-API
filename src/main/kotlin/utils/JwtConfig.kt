package utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import model.PrincipalJwt
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object JwtConfig {
    private const val secret = "E7u9EqbgH]\\6:Swna%A_G4.=\$:Q\\yz[3"
    private const val issuer = "com.jakode.slice-api"
    private const val audience = "jwt-audience"
    private val validationTime = Duration.minutes(10)
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    /** Produce a token for this combination of name and password */
    fun generateToken(principal: PrincipalJwt): String = JWT.create()
        .withSubject("Authentication")
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("name", principal.name)
        .withClaim("phone", principal.phone?.value)
        .withClaim("email", principal.email?.value)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    /** Calculate the expiration Date based on current time + the given validity */
    private fun getExpiration() = Date(System.currentTimeMillis() + validationTime.inWholeMilliseconds)
}