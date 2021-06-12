package database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object Cats : UUIDTable() {
    val name = varchar("name", 50).uniqueIndex()
    val age = integer("age").default(0)
}