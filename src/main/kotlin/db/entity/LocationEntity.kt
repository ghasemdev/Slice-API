package db.entity

import db.table.LocationsTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LocationEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<LocationEntity>(LocationsTable)

    // referenced on user table
    var user by UserEntity referencedOn LocationsTable.user

    var locationName by LocationsTable.locationName
    var latitude by LocationsTable.latitude
    var longitude by LocationsTable.longitude
}