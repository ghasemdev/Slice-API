package routes.wishlist

import db.DatabaseFactory.dbQuery
import db.entity.UserEntity
import db.entity.asFood
import db.table.WishlistTable
import model.Food
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import utils.DuplicateRecordException
import utils.RecordNotExistException
import java.util.*

class WishlistServiceImp : WishlistService {
    override suspend fun getAll(userId: UUID, limit: Int, page: Long): List<Food> = dbQuery {
        val user = UserEntity[userId]
        user.interestedFoods.toList().map { it.asFood }
    }

    override suspend fun add(userId: UUID, foodsId: Long): Unit = dbQuery {
        try {
            WishlistTable.insert { wishlist ->
                wishlist[user] = userId
                wishlist[food] = foodsId
            }
        } catch (exception: Exception) {
            exception.message?.let {
                if (it.contains("already exists")) {
                    throw DuplicateRecordException("This food has already been added to wishlist")
                } else if (it.contains("is not present in table")) {
                    throw RecordNotExistException("This food doesn't exist")
                }
            }
        }
    }

    override suspend fun delete(userId: UUID, foodsId: Long): Unit = dbQuery {
        val validation = WishlistTable.select {
            (WishlistTable.user eq userId) and (WishlistTable.food eq foodsId)
        }.firstOrNull()

        if (validation != null) {
            WishlistTable.deleteWhere { (WishlistTable.user eq userId) and (WishlistTable.food eq foodsId) }
        } else {
            throw RecordNotExistException("This food has not been added to wishlist")
        }
    }
}