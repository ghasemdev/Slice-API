package db.table

import model.FoodSize
import org.jetbrains.exposed.dao.id.IntIdTable
import utils.PGEnum

object FoodSizesTable: IntIdTable() {
    val size = customEnumeration("size", "FoodSize", {value -> FoodSize.valueOf(value as String)}, { PGEnum("FoodSize", it) })
}