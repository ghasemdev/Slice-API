//package database.table
//
//import org.jetbrains.exposed.sql.Table
//import org.jetbrains.exposed.sql.`java-time`.CurrentDateTime
//import org.jetbrains.exposed.sql.`java-time`.datetime
//
//object Otp : Table() {
//    val userId = reference("userId", User)
//    val otp = varchar("otp", 47).uniqueIndex()
//    val createAt = datetime("createAt").defaultExpression(CurrentDateTime())
//
//    override val primaryKey = PrimaryKey(userId)
//}