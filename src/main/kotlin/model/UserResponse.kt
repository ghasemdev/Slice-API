package model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val token: String, val user: User)

@Serializable
data class User(
    val isMarketer: Boolean = false,
    val nickname: String,
    val profilePicture: String? = null,
    val phone: Phone? = null,
    val email: Email? = null,
    val locations: List<Location>? = null
)

@Serializable
class Location(val locationName: String, val latitude: Float, val longitude: Float)

@JvmInline
@Serializable
value class Phone(val value: String) {
    /** check is a phone number*/
    fun isValid() = value.isNotEmpty() && value.matches(Regex("09\\d{9}"))
}

@JvmInline
@Serializable
value class Email(val value: String) {
    /** check is a valid email*/
    fun isValid() = value.isNotEmpty() && value.matches(Regex("^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$"))
}