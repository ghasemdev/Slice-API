package db.table

import org.jetbrains.exposed.dao.id.LongIdTable

object User : LongIdTable() {
    val isMarketer = bool("isMarketer").default(false)
    val nickname = varchar("nickname",50)
    val profilePicture = binary("profilePicture").nullable()
    val phone = varchar("phone",11).uniqueIndex().nullable()
    val email = varchar("email",50).uniqueIndex().nullable()
}