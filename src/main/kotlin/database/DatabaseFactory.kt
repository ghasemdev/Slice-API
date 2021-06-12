package database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.net.URI

object DatabaseFactory {
    private val host: String
    private val port: Int
    private val dbName: String
    private val dbUser: String
    private val dbPassword: String

    init {
        val dbUrl = System.getenv("DATABASE_URL")
        if (dbUrl != null) {
            val dbUri = URI(dbUrl)
            host = dbUri.host
            port = dbUri.port
            dbName = dbUri.path.substring(1)
            val userInfo = dbUri.userInfo.split(":")
            dbUser = userInfo[0]
            dbPassword = userInfo[1]
        } else {
            host = System.getenv("DB_HOST") ?: "localhost"
            port = System.getenv("DB_PORT")?.toInt() ?: 5555
            dbName = System.getenv("DB_NAME") ?: "slice_db"
            dbUser = System.getenv("DB_USER") ?: "jakode2020"
            dbPassword = System.getenv("DB_PASSWORD") ?: "e-UuWUX8=-(W)RGG"
        }
    }

    fun connect() = HikariDataSource(HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://$host:$port/$dbName"
        driverClassName = "org.postgresql.Driver"
        username = dbUser
        password = dbPassword
        maximumPoolSize = 20
        validate()
    })

    suspend fun <T> dbQuery(context: CoroutineDispatcher = Dispatchers.IO, block: suspend () -> T): T =
        newSuspendedTransaction(context) { block() }
}