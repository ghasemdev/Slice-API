package database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.`java-time`.CurrentDateTime
import org.jetbrains.exposed.sql.`java-time`.dateTimeParam
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object Otps : LongIdTable() {
    val otp = varchar("otp", 47).uniqueIndex()
    val create_at = datetime("create_at").defaultExpression(CurrentDateTime())
}