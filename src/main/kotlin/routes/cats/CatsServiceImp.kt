package routes.cats

import database.DatabaseFactory.dbQuery
import database.entity.CatEntity
import database.table.Cats
import model.Cat
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.*

class CatsServiceImp : CatsService {

    override suspend fun create(cat: Cat): UUID = dbQuery { CatEntity.new(cat) }.id.value

    override suspend fun getAll(): List<Cat> = dbQuery { Cats.selectAll().map { it.asCat() } }

    override suspend fun findById(id: String): Cat? = dbQuery { CatEntity.findById(UUID.fromString(id))?.asCat() }

    override suspend fun delete(id: String): Unit = dbQuery {
        Cats.deleteWhere { Cats.id eq UUID.fromString(id) }
    }

    override suspend fun update(cat: Cat): Unit = dbQuery {
        Cats.update({ Cats.id eq UUID.fromString(cat.id) }) { table ->
            cat.name?.let { table[name] = it }
            cat.age?.let { table[age] = it }
        }
    }
}

fun ResultRow.asCat() = Cat(this[Cats.id].value.toString(), this[Cats.name], this[Cats.age])
fun CatEntity.asCat() = Cat(this.id.value.toString(), this.name, this.age)

fun CatEntity.Companion.new(cat: Cat) = CatEntity.new(UUID.randomUUID()) {
    cat.name?.let { this.name = it }
    cat.age?.let { this.age = it }
}