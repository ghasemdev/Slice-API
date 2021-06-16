package model

import io.ktor.application.*
import io.ktor.auth.*

data class JwtUser(
    val id: String,
    val name: String
) : Principal

val ApplicationCall.tokenParser get() = authentication.principal<JwtUser>()