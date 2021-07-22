package utils

import java.io.File

object CSV {
    fun readFile(
        delimiter: Char = ',',
        path: String = "src/main/resources/scv/",
        name: String,
        function: (List<String>) -> Unit
    ) {
        File(path + name).forEachLine {
            val line = it.split(delimiter)
            function(line)
        }
    }
}