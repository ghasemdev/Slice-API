package db.migration

import db.entity.CommentEntity
import db.entity.FoodEntity
import db.entity.LocationEntity
import db.entity.UserEntity
import db.table.*
import model.FoodCategory
import model.FoodSize
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import utils.CSV
import java.util.*

class V1__create_tables : BaseJavaMigration() {
    private lateinit var admin: UserEntity

    override fun migrate(context: Context?) {
        transaction {
            foodCategory()
            foodSize()

            SchemaUtils.create(
                UsersTable,
                OtpsTable,
                LocationsTable,
                FoodsTable,
                WishlistTable,
                CommentsTable
            )

            createAdmin()
            importData()
            addComment()
        }
    }

    private fun Transaction.foodCategory() {
        exec("CREATE TYPE FoodCategory AS ENUM ('Pizza', 'Sandwich', 'Beverage', 'Salad', 'Appetizer', 'Sauce');")
    }

    private fun Transaction.foodSize() {
        exec("CREATE TYPE FoodSize AS ENUM ('Small', 'Medium', 'Large', 'Extralarge');")
    }

    private fun createAdmin() {
        admin = UserEntity.new(UUID.randomUUID()) {
            isMarketer = true
            nickname = "jakode"
            email = "aQqkDl/AqLw1yOs5DTm2T0R6QzCT+MmLtbEmHbFUQHc="
        }
        LocationEntity.new {
            this.user = admin
            locationName = "Chenaran, Razavi Khorasan Province, Iran"
            latitude = 36.647764F
            longitude = 59.109951F
        }
    }

    private fun importData() {
        CSV.readFile(delimiter = ';', path = "src/main/resources/csv/", name = "pizza.txt") { row ->
            FoodEntity.new {
                isActive = row[1].toInt() == 1
                category = FoodCategory.getEnum(row[2])
                size = if (row[3] == "Null") null else FoodSize.getEnum(row[3])
                name = row[4]
                picture = row[5]
                quantity = row[6].toInt()
                salesNumber = row[7].toInt()
                price = row[8].toFloat()
                discount = row[9].toInt()
                score = row[10].toFloat()
                votersNumber = row[11].toInt()
                details = row[12]
                preparationTime = row[13].toInt()
                volume = if (row[14] == "Null") null else row[14]
            }
        }
    }

    private fun addComment() {
        CommentEntity.new {
            this.user = admin
            food = FoodEntity[6]
            content = "I like that pizza :)"
        }
    }
}