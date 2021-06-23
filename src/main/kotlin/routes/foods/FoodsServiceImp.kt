package routes.foods

import db.DatabaseFactory.dbQuery
import db.entity.FoodEntity
import db.entity.asFood
import db.table.FoodsTable
import db.table.asFoodItems
import model.Food
import model.FoodItems
import model.FoodsFilter
import model.OrderBy
import org.jetbrains.exposed.sql.*
import org.koin.java.KoinJavaComponent.inject
import routes.wishlist.WishlistService
import utils.RecordNotExistException
import java.util.*

class FoodsServiceImp : FoodsService {
    val wishlistService: WishlistService by inject(WishlistService::class.java)

    override suspend fun getAll(filter: FoodsFilter): List<FoodItems> = dbQuery {
        val query = FoodsTable.selectAll()
        with(filter) {
            category?.let { category -> query.andWhere { FoodsTable.category eq category } }
            search?.let { name -> query.andWhere { FoodsTable.name.lowerCase() like "%${name.lowercase()}%" } }
            isActive?.let { isActive -> query.andWhere { FoodsTable.isActive eq isActive } }
            size?.let { size -> query.andWhere { FoodsTable.size eq size } }
            price?.let { (from, to) -> query.andWhere { FoodsTable.price.between(from, to) } }
            score?.let { (from, to) -> query.andWhere { FoodsTable.score.between(from, to) } }
            time?.let { (from, to) -> query.andWhere { FoodsTable.preparationTime.between(from, to) } }
            order?.let { (orderBy, sortOrder) ->
                query.orderBy(
                    when (orderBy) {
                        OrderBy.Name -> FoodsTable.name
                        OrderBy.Price -> FoodsTable.price
                        OrderBy.Score -> FoodsTable.score
                        OrderBy.Time -> FoodsTable.preparationTime
                    }, sortOrder
                )
            }
        }
        query.limit(filter.limit, filter.offset).toList().map { it.asFoodItems }
    }

    override suspend fun get(userId: UUID, foodId: Long): Food = dbQuery {
        try {
            val food = FoodEntity[foodId]
            food.asFood.apply {
                this.inWishlist = wishlistService.inWishlist(userId, foodId)
            }
        } catch (exception: Exception) {
            throw RecordNotExistException("This food doesn't exist")
        }
    }

    override suspend fun getSpecialOffer(limit: Int, page: Long): List<FoodItems> = dbQuery {
        val foodsWithDiscount = FoodsTable.select { FoodsTable.discount neq 0 }
        foods(foodsWithDiscount, FoodsTable.discount.castTo(FloatColumnType()), FoodsTable.price, limit, page)
    }

    override suspend fun getBestSelling(limit: Int, page: Long): List<FoodItems> = dbQuery {
        val foodsSold = FoodsTable.select { FoodsTable.salesNumber neq 0 }
        foods(foodsSold, FoodsTable.salesNumber.castTo(FloatColumnType()), FoodsTable.price, limit, page)
    }

    override suspend fun getHighestScores(limit: Int, page: Long): List<FoodItems> = dbQuery {
        val ratedFoods = FoodsTable.select { FoodsTable.score neq 0F }
        foods(ratedFoods, FoodsTable.votersNumber.castTo(FloatColumnType()), FoodsTable.score, limit, page)
    }

    private fun <T> foods(
        query: Query,
        firstColumn: ExpressionWithColumnType<T>,
        secondColumn: Column<T>,
        limit: Int,
        page: Long
    ) = query
        .orderBy(Expression.build { firstColumn * secondColumn }, SortOrder.DESC)
        .limit(limit, (page - 1) * limit).toList().map { it.asFoodItems }
}