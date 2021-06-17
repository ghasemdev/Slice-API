package db.migration

import db.table.*
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
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
    }

    private fun Transaction.foodSize() {
        exec("CREATE TYPE FoodSize AS ENUM ('Small', 'Medium', 'Large', 'ExtraLarge');")
    }
}