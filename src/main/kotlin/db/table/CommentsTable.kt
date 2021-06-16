package db.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.CurrentDateTime
import org.jetbrains.exposed.sql.`java-time`.datetime

object CommentsTable : LongIdTable() {
    val user = reference("user", UsersTable, onDelete = ReferenceOption.CASCADE)
    val food = reference("food", FoodsTable, onDelete = ReferenceOption.CASCADE)
    val content = varchar("content",500)
    val createAt = datetime("createAt").defaultExpression(CurrentDateTime())

    override val primaryKey = PrimaryKey(id, user, food)
}