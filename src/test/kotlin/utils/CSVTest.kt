package utils

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import model.FoodItems
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
                    name = it[4],
                    picture = it[5],
                    price = it[8].toFloat(),
                    discount = it[9].toInt(),
                    score = it[10].toFloat(),
                    details = it[12],
                    preparationTime = it[13].toInt(),
                )
            )
            println(foods.last())
        }
        assertThat(foods.size).isEqualTo(14)
    }
}