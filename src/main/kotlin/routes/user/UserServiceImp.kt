package routes.user

import db.DatabaseFactory.dbQuery
import db.entity.UserEntity
import db.entity.asUser
import db.entity.new
import db.table.UsersTable
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
        userEntity?.let { return@dbQuery findUser(it) }
    }

    override suspend fun findByEmail(email: Email): UserResponse? = dbQuery {
        val userEntity = UserEntity.find { UsersTable.email eq email.value }.firstOrNull()
        userEntity?.let { return@dbQuery findUser(it) }
    }

    private fun findUser(userEntity: UserEntity): UserResponse {
        val id = userEntity.id.value.toString()
        val name = userEntity.nickname
        val token = JwtConfig.generateToken(JwtUser(id, name)) // generate token
        return UserResponse(token, userEntity.asUser)
    }

    override suspend fun delete(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun update(user: User) {
        TODO("Not yet implemented")
    }
}