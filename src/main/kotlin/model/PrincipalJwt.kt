package model

import io.ktor.application.*
import io.ktor.auth.*

data class PrincipalJwt(
    val name: String,
    val phone: Phone? = null,
    val email: Email? = null
) : Principal

val ApplicationCall.tokenParser get() = authentication.principal<PrincipalJwt>()