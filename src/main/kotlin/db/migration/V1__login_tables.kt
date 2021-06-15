package db.migration

import db.table.LocationsTable
import db.table.OtpsTable
import db.table.UsersTable
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V1__login_tables : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.create(UsersTable, OtpsTable, LocationsTable)
        }
    }
}