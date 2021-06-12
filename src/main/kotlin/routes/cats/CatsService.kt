package routes.cats

import model.Cat
import java.util.*

interface CatsService {
    suspend fun create(cat: Cat): UUID
    suspend fun getAll(): List<Cat>
    suspend fun findById(id: String): Cat?
    suspend fun delete(id: String)
    suspend fun update(cat: Cat)
}