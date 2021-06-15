package db.table

import org.jetbrains.exposed.dao.id.UUIDTable

object UsersTable : UUIDTable() {
    val isMarketer = bool("isMarketer").default(false)
    val nickname = varchar("nickname",50)
    val profilePicture = binary("profilePicture").nullable()
    val phone = varchar("phone",24).uniqueIndex().nullable()
    val email = varchar("email",88).uniqueIndex().nullable()
}