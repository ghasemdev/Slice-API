package routes.user

import kpy.struct.Email
import kpy.struct.Phone
import model.User
import model.UserResponse

interface UserService {
    suspend fun create(user: User): UserResponse?
    suspend fun findByPhone(phone: Phone): UserResponse?
    suspend fun findByEmail(email: Email): UserResponse?
}