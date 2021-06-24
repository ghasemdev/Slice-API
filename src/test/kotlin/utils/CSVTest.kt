package utils

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import model.FoodCategory
import model.FoodItems
import model.FoodSize
import org.junit.jupiter.api.Test

class CSVTest {

    @Test
    fun `read csv file`() = runBlocking {
        val foods = mutableListOf<FoodItems>()
        CSV.readFile(delimiter = ';', path = "src/test/resources/csv/", name = "foods.txt") {
            foods.add(
                FoodItems(
                    id = it[0].toLong(),
                    isActive = it[1].toInt() == 1,
                    category = FoodCategory.getEnum(it[2]),
                    size = FoodSize.getEnum(it[3]),
                    name = it[4],
                    picture = it[5],
                    quantity = it[6].toInt(),
                    salesNumber = it[7].toInt(),
                    price = it[8].toFloat(),
                    discount = it[9].toInt(),
                    score = it[10].toFloat(),
                    votersNumber = it[11].toInt(),
                    details = it[12],
                    preparationTime = it[13].toInt(),
                    volume = it[14]
                )
            )
            println(foods.last())
        }
        assertThat(foods.size).isEqualTo(14)
    }
}