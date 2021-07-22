package routes.user

import db.DatabaseFactory.dbQuery
import db.entity.UserEntity
import db.entity.asUser
import db.entity.new
import db.table.UsersTable
import kpy.struct.Email
import kpy.struct.Phone
import model.*
import utils.JwtConfig

class UserServiceImp : UserService {
    override suspend fun create(user: User): UserResponse? = dbQuery {
        try {
            // create user in database
            val userEntity = UserEntity.new(user)
            val id = userEntity.id.value.toString()
            val name = userEntity.nickname
            // generate token
            val token = JwtConfig.generateToken(JwtUser(id, name))
            UserResponse(token, userEntity.asUser)
        } catch (Exception: Exception) {
            null
        }
    }

    override suspend fun findByPhone(phone: Phone): UserResponse? = dbQuery {
        val userEntity = UserEntity.find { UsersTable.phone eq phone.value }.firstOrNull()
        userEntity?.let { return@dbQuery findUser(it) } ?: return@dbQuery null
    }

    override suspend fun findByEmail(email: Email): UserResponse? = dbQuery {
        val userEntity = UserEntity.find { UsersTable.email eq email.value }.firstOrNull()
        userEntity?.let { return@dbQuery findUser(it) } ?: return@dbQuery null
    }

    private fun findUser(userEntity: UserEntity): UserResponse {
        val id = userEntity.id.value.toString()
        val name = userEntity.nickname
        val token = JwtConfig.generateToken(JwtUser(id, name)) // generate token
        return UserResponse(token, userEntity.asUser)
    }
}