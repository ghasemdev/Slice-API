package db.entity

import db.table.*
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FoodEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<FoodEntity>(FoodsTable)

    var isActive by FoodsTable.isActive
    var category by FoodsTable.category
    var size by FoodsTable.size
    var name by FoodsTable.name
    var picture by FoodsTable.picture
    var quantity by FoodsTable.quantity
    var salesNumber by FoodsTable.salesNumber
    var price by FoodsTable.price
    var discount by FoodsTable.discount
    var score by FoodsTable.score
    var votersNumber by FoodsTable.votersNumber
    var details by FoodsTable.details
    var preparationTime by FoodsTable.preparationTime
    var volume by FoodsTable.volume

    // referenced on comments table (one to many)
    val comments by CommentEntity referrersOn CommentsTable.food
}