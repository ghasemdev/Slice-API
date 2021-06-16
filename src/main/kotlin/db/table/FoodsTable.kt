package db.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object FoodsTable : LongIdTable() {
    val isActive = bool("isActive").default(true)
    val category = reference("category", FoodCategoriesTable, onDelete = ReferenceOption.SET_NULL)
    val size = reference("size", FoodSizesTable, onDelete = ReferenceOption.SET_NULL).nullable()
    val name = varchar("name", 50)
    val picture = varchar("picture", 100)
    val count = integer("count").default(1)
    val salesNumber = integer("salesNumber").default(0)
    val price = float("price")
    val discount = integer("discount").default(0)
    val score = float("score").default(0F)
    val votersNumber = integer("votersNumber").default(0)
    val details = varchar("details", 500)
    val preparationTime = integer("preparationTime").nullable()
    val volume = varchar("volume", 50).nullable()
}