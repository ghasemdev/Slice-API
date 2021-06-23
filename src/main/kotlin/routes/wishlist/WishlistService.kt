package routes.wishlist

import model.FoodItems
import java.util.*

interface WishlistService {
    suspend fun getAll(userId: UUID, limit: Int, page: Long): List<FoodItems>
    suspend fun add(userId: UUID, foodId: Long)
    suspend fun delete(userId: UUID, foodId: Long)
    suspend fun inWishlist(userId: UUID, foodId: Long): Boolean
}