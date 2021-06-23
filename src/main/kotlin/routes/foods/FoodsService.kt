package routes.foods

import model.*

interface FoodsService {
    suspend fun getAll(filter: FoodsFilter): List<Food>
    suspend fun getSpecialOffer(limit: Int, page: Long): List<Food>
    suspend fun getBestSelling(limit: Int, page: Long): List<Food>
    suspend fun getHighestScores(limit: Int, page: Long): List<Food>
}