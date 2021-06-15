package routes.user

import model.Email
import model.Phone
import model.User

interface UserService {
    suspend fun create(user: User): Any
    suspend fun findByPhone(phone: Phone): User?
    suspend fun findByEmail(email: Email): User?
    suspend fun delete(id: String)
    suspend fun update(user: User)
}