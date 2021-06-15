package model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ErrorResponse(val value: String)