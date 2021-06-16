package db.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object WishlistTable : Table() {
    val user = reference("user", UsersTable, onDelete = ReferenceOption.CASCADE)
    val food = reference("food", FoodsTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(user, food)
}