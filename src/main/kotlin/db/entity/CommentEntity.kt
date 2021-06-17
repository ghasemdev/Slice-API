package db.entity

import db.table.CommentsTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CommentEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<CommentEntity>(CommentsTable)

    // referenced on user table
    var user by UserEntity referencedOn CommentsTable.user

    // referenced on food table
    var food by FoodEntity referencedOn CommentsTable.food
    var content by CommentsTable.content
    var createAt by CommentsTable.createAt
}