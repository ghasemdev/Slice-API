package db.entity

import db.table.CommentsTable
import db.table.FoodsTable
import model.Food
import model.FoodItems
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

val FoodEntity.asFoodItems: FoodItems
    get() = FoodItems(
        id = this.id.value,
        isActive = this.isActive,
        name = this.name,
        picture = this.picture,
        price = this.price,
        discount = this.discount,
        score = this.score,
        details = this.details,
        preparationTime = this.preparationTime,
    )

val FoodEntity.asFood: Food
    get() = Food(
        id = this.id.value,
        isActive = this.isActive,
        category = this.category,
        size = this.size,
        name = this.name,
        picture = this.picture,
        quantity = this.quantity,
        salesNumber = this.salesNumber,
        price = this.price,
        discount = this.discount,
        score = this.score,
        votersNumber = this.votersNumber,
        details = this.details,
        preparationTime = this.preparationTime,
        volume = this.volume,
        comments = this.comments.toList().map { it.asComment }
    )