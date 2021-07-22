package model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kpy.struct.Email
import kpy.struct.Phone

@Serializable
data class UserResponse(val token: String, val user: User)

@Serializable
data class User(
    val isMarketer: Boolean = false,
    val nickname: String,
    val profilePicture: String? = null,
    var phone: @Contextual Phone? = null,
    var email: @Contextual Email? = null,
    val locations: List<Location>? = null
)

@Serializable
class Location(val locationName: String, val latitude: Float, val longitude: Float)