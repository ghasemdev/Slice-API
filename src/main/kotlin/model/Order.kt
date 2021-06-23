package model

import org.jetbrains.exposed.sql.SortOrder

typealias Order = Pair<OrderBy, SortOrder>

enum class OrderBy {
    Name, Price, Score, Time;

    companion object {
        fun getEnum(value: String) = valueOf(value.trim().lowercase().replaceFirstChar { it.uppercase() })
    }
}