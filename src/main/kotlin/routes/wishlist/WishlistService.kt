package routes.wishlist

import model.Food
import java.util.*

interface WishlistService {
    suspend fun getAll(userId: UUID, limit: Int, page: Long): List<Food>
    suspend fun add(userId: UUID, foodsId: Long)
    suspend fun delete(userId: UUID, foodsId: Long)
}