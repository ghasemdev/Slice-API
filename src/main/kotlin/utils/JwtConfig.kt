package utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.jwt.*
import model.JwtUser
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object JwtConfig {
    private const val jwtSecret = "7*6v/Yf}=v;=#H7SA@!L{dZJk2Awu6h:y6c`c~_-v\$9-cyyun@Va;^A<V2f%pqNC"
    private const val jwtIssuer = "com.jakode"
    private const val jwtRealm = "com.jakode.slice"

    private const val CLAIM_USER_ID = "id"
    private const val CLAIM_USER_NAME = "name"

    private val validationTime = Duration.days(10)
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    private val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(jwtIssuer)
        .build()

    /** Produce a token for this combination of name and password */
    fun generateToken(jwtUser: JwtUser): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM_USER_ID, jwtUser.id)
        .withClaim(CLAIM_USER_NAME, jwtUser.name)
//        .withExpiresAt(getExpiration())
        .sign(algorithm)

    fun configureJwt(configuration: JWTAuthenticationProvider.Configuration) = with(configuration) {
        verifier(verifier)
        realm = jwtRealm
        validate {
            val id = it.payload.getClaim(CLAIM_USER_ID).asString()
            val name = it.payload.getClaim(CLAIM_USER_NAME).asString()

            if (id != null && name != null) {
                JwtUser(id, name)
            } else {
                null
            }
        }
    }

    /** Calculate the expiration Date based on current time + the given validity */
    private fun getExpiration() = Date(System.currentTimeMillis() + validationTime.inWholeMilliseconds)
}