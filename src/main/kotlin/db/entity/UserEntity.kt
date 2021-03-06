package db.entity

import db.table.LocationsTable
import db.table.UsersTable
import db.table.WishlistTable
import kpy.struct.Email
import kpy.struct.Phone
import kpy.util.crypto.Crypto
import model.*
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UsersTable)

    var isMarketer by UsersTable.isMarketer
    var nickname by UsersTable.nickname
    var profilePicture by UsersTable.profilePicture
    var phone by UsersTable.phone
    var email by UsersTable.email

    // referenced on location table
    val locations by LocationEntity referrersOn LocationsTable.user

    // many to many relation (Each users has like n)
    var interestedFoods by FoodEntity via WishlistTable
}

fun UserEntity.Companion.new(user: User) = UserEntity.new(UUID.randomUUID()) {
    this.nickname = user.nickname
    user.phone?.let { this.phone = it.value }
    user.email?.let { this.email = it.value }
}

private val crypto: Crypto by inject(Crypto::class.java)

val UserEntity.asUser: User
    get() = User(
        isMarketer = this.isMarketer,
        nickname = this.nickname,
        profilePicture = profilePicture.let { "/files/pictures/profile/${crypto.urlEncoding(this.id.toString())}" },
        phone = this.phone?.let { Phone(crypto.decrypt(it)) },
        email = this.email?.let { Email(crypto.decrypt(it)) },
        locations = this.locations.toList().map {
            Location(it.locationName, it.latitude, it.longitude)
        }
    )

val UserEntity.asUserComment: UserComment
    get() = UserComment(
        isMarketer = this.isMarketer,
        nickname = this.nickname,
        profilePicture = profilePicture.let { "/files/pictures/profile/${crypto.urlEncoding(this.id.toString())}" },
    )