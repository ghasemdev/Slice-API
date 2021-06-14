package db.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.CurrentDateTime
import org.jetbrains.exposed.sql.`java-time`.datetime

object Otp : UUIDTable() {
    val phone = varchar("phone", 47).nullable()
    val email = varchar("email", 47).nullable()
    val otp = varchar("otp", 47).uniqueIndex()
    val createAt = datetime("createAt").defaultExpression(CurrentDateTime())
}