package model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import utils.PreparationTime
import utils.Price
import utils.Score

@Serializable
data class Food(
    val id: Long,
    var inWishlist: Boolean = false,
    val isActive: Boolean = true,
    val category: FoodCategory,
    val size: FoodSize? = null,
    val name: String,
    val picture: String,
    val quantity: Int = 1,
    val salesNumber: Int = 0,
    val price: Float,
    val discount: Int = 0,
    val score: Float = 0F,
    val votersNumber: Int = 0,
    val details: String,
    val preparationTime: Int = 0,
    val volume: String? = null,
    val comments: List<Comment>? = null
)

@Serializable
data class Comment(val user: UserComment, val content: String, @Contextual val createAt: String)

@Serializable
data class UserComment(
    val isMarketer: Boolean = false,
    val nickname: String,
    val profilePicture: String? = null,
)

@Serializable
data class FoodsResponse(val page: Long, val count: Int, val foodItems: List<FoodItems>)

@Serializable
data class FoodItems(
    val id: Long,
    val isActive: Boolean = true,
    val name: String,
    val picture: String,
    val price: Float,
    val discount: Int = 0,
    val score: Float = 0F,
    val details: String,
    val preparationTime: Int = 0,
)

data class FoodsFilter(
    val limit: Int = 10,
    val page: Long = 1,
    val category: FoodCategory? = null,
    val search: String? = null,
    val isActive: Boolean? = null,
    val size: FoodSize? = null,
    val price: Price? = null,
    val score: Score? = null,
    val time: PreparationTime? = null,
    val order: Order? = null
) {
    val offset get() = (page - 1) * limit
}

enum class FoodCategory {
    Pizza, Sandwich, Beverage, Salad, Appetizer, Sauce;

    companion object {
        fun getEnum(value: String) = valueOf(value.trim().lowercase().replaceFirstChar { it.uppercase() })
    }
}

enum class FoodSize {
    Small, Medium, Large, Extralarge;

    companion object {
        fun getEnum(value: String) = valueOf(value.trim().lowercase().replaceFirstChar { it.uppercase() })
    }
}