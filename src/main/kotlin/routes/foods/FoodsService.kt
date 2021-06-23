package routes.foods

import model.Food
import model.FoodItems
import model.FoodsFilter
import java.util.*

interface FoodsService {
    suspend fun getAll(filter: FoodsFilter): List<FoodItems>
    suspend fun get(userId: UUID, foodId: Long): Food
    suspend fun getSpecialOffer(limit: Int, page: Long): List<FoodItems>
    suspend fun getBestSelling(limit: Int, page: Long): List<FoodItems>
    suspend fun getHighestScores(limit: Int, page: Long): List<FoodItems>
}