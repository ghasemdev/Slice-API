package db.table

import model.FoodCategory
import model.FoodItems
import model.FoodSize
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import utils.PGEnum

object FoodsTable : LongIdTable() {
    val isActive = bool("isActive").default(true)

    val category = customEnumeration(
        "category",
        "FoodCategory",
        { value -> FoodCategory.valueOf(value as String) },
        { PGEnum("FoodCategory", it) })
    val size = customEnumeration(
        "size",
        "FoodSize",
        { value -> FoodSize.valueOf(value as String) },
        { PGEnum("FoodSize", it) }).nullable()

    val name = varchar("name", 50)
    val picture = varchar("picture", 255)
    val quantity = integer("quantity").default(1)
    val salesNumber = integer("salesNumber").default(0)
    val price = float("price")
    val discount = integer("discount").default(0)
    val score = float("score").default(0F)
    val votersNumber = integer("votersNumber").default(0)
    val details = varchar("details", 500)
    val preparationTime = integer("preparationTime").default(0)
    val volume = varchar("volume", 50).nullable()
}

val ResultRow.asFoodItems: FoodItems
    get() = FoodItems(
        id = this[FoodsTable.id].value,
        isActive = this[FoodsTable.isActive],
        name = this[FoodsTable.name],
        picture = this[FoodsTable.picture],
        price = this[FoodsTable.price],
        discount = this[FoodsTable.discount],
        score = this[FoodsTable.score],
        details = this[FoodsTable.details],
        preparationTime = this[FoodsTable.preparationTime],
    )