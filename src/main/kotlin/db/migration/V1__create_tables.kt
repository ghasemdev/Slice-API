package db.migration

import db.table.*
import model.FoodCategory
import model.FoodSize
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class V1__create_tables : BaseJavaMigration() {
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
        }
    }

    private fun Transaction.foodCategory() {
        exec("CREATE TYPE FoodCategory AS ENUM ('Pizza', 'Sandwich', 'Beverage', 'Salad', 'Appetizer', 'Sauce');")
        SchemaUtils.create(FoodCategoriesTable)
        FoodCategoriesTable.apply {
            insert { it[category] = FoodCategory.Pizza }
            insert { it[category] = FoodCategory.Sandwich }
            insert { it[category] = FoodCategory.Beverage }
            insert { it[category] = FoodCategory.Salad }
            insert { it[category] = FoodCategory.Appetizer }
            insert { it[category] = FoodCategory.Sauce }
        }
    }

    private fun Transaction.foodSize() {
        exec("CREATE TYPE FoodSize AS ENUM ('Small', 'Medium', 'Large', 'ExtraLarge');")
        SchemaUtils.create(FoodSizesTable)
        FoodSizesTable.apply {
            insert { it[size] = FoodSize.Small }
            insert { it[size] = FoodSize.Medium }
            insert { it[size] = FoodSize.Large }
            insert { it[size] = FoodSize.ExtraLarge }
        }
    }
}