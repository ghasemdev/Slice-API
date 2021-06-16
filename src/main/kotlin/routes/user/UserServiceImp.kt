package routes.user

import db.DatabaseFactory.dbQuery
import db.entity.UserEntity
import db.entity.asUser
import db.entity.new
import model.*
import utils.JwtConfig

class UserServiceImp : UserService {
    override suspend fun create(user: User): Any = dbQuery {
        try {
            // create user in database
            val userEntity = UserEntity.new(user)
            val id = userEntity.id.value.toString()
            val name = userEntity.nickname
            // generate token
            val token = JwtConfig.generateToken(JwtUser(id, name))
            UserResponse(token, userEntity.asUser)
        } catch (Exception: Exception) {
            ErrorResponse("A user with this email or phone number is already registered!!!")
        }
    }

    override suspend fun findByPhone(phone: Phone): User? {
        TODO("Not yet implemented")
    }

    override suspend fun findByEmail(email: Email): User? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun update(user: User) {
        TODO("Not yet implemented")
    }
}