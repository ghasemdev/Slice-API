package routes.user

import model.Email
import model.Phone
import model.User
import model.UserResponse

interface UserService {
    suspend fun create(user: User): UserResponse?
    suspend fun findByPhone(phone: Phone): UserResponse?
    suspend fun findByEmail(email: Email): UserResponse?
    suspend fun delete(id: String)
    suspend fun update(user: User)
}