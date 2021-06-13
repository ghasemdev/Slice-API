//package database.table
//
//import org.jetbrains.exposed.dao.id.LongIdTable
//
//object Locations : LongIdTable() {
//    val userId = reference("userId", User)
//    val name = varchar("name", 50).uniqueIndex()
//    val latitude = float("latitude")
//    val longitude = float("longitude")
//
//    override val primaryKey = PrimaryKey(id, userId)
//}