package model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val token: String, val user: User)

@Serializable
data class User(
    val isMarketer: Boolean = false,
    val nickname: String,
    val profilePicture: String? = null,
    var phone: Phone? = null,
    var email: Email? = null,
    val locations: List<Location>? = null
)

@Serializable
class Location(val locationName: String, val latitude: Float, val longitude: Float)

@JvmInline
@Serializable
value class Phone(val value: String) {
    companion object {
        /** check is a phone number*/
        fun isValid(phone: String) = phone.isNotEmpty() && phone.matches(Regex("09\\d{9}"))
    }
}

@JvmInline
@Serializable
value class Email(val value: String) {
    companion object {
        /** check is a valid email*/
        fun isValid(email: String) = email.isNotEmpty() && email.matches(Regex("^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$"))
    }
}