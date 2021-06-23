package utils

import java.io.File

object CSV {
    /*suspend*/ fun readFile(
        delimiter: Char = ',',
        path: String = "src/main/resources/scv/",
        name: String,
        function: (List<String>) -> Unit
    ) /*= withContext(Dispatchers.IO)*/ {
        File(path + name).forEachLine {
            val line = it.split(delimiter)
            function(line)
        }
    }
}