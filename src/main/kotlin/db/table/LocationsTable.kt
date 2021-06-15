package db.table

import org.jetbrains.exposed.dao.id.LongIdTable

object LocationsTable : LongIdTable() {
    val user = reference("user", UsersTable)
    val locationName = varchar("locationName", 50)
    val latitude = float("latitude")
    val longitude = float("longitude")

    override val primaryKey = PrimaryKey(id, user)
}