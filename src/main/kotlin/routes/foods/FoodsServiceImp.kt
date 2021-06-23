package routes.foods

import db.DatabaseFactory.dbQuery
import db.table.FoodsTable
import db.table.asFood
import model.*
import org.jetbrains.exposed.sql.*

class FoodsServiceImp : FoodsService {
    override suspend fun getAll(filter: FoodsFilter): List<Food> = dbQuery {
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
        query.limit(filter.limit, filter.offset).toList().map { it.asFood }
    }

    override suspend fun getSpecialOffer(limit: Int, page: Long): List<Food> = dbQuery {
        val foodsWithDiscount = FoodsTable.select { FoodsTable.discount neq 0 }
        foods(foodsWithDiscount, FoodsTable.discount.castTo(FloatColumnType()), FoodsTable.price, limit, page)
    }

    override suspend fun getBestSelling(limit: Int, page: Long): List<Food> = dbQuery {
        val foodsSold = FoodsTable.select { FoodsTable.salesNumber neq 0 }
        foods(foodsSold, FoodsTable.salesNumber.castTo(FloatColumnType()), FoodsTable.price, limit, page)
    }

    override suspend fun getHighestScores(limit: Int, page: Long): List<Food> = dbQuery {
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
        .limit(limit, (page - 1) * limit).toList().map { it.asFood }
}