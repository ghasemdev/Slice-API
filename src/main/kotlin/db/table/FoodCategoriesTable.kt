package db.table

import model.FoodCategory
import org.jetbrains.exposed.dao.id.IntIdTable
import utils.PGEnum

object FoodCategoriesTable : IntIdTable() {
    val category = customEnumeration("category", "FoodCategory", {value -> FoodCategory.valueOf(value as String)}, { PGEnum("FoodCategory", it) })
}