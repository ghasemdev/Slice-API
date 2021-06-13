package db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory
import java.net.URI
import javax.sql.DataSource

object DatabaseFactory {
    private val host: String
    private val port: Int
    private val dbName: String
    private val dbUser: String
    private val dbPassword: String

    private val log = LoggerFactory.getLogger(this::class.java)

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

    fun connectAndMigrate() {
        log.info("Initialising database")
        val pool = hikari()
        Database.connect(pool)
        runFlyway(pool)
    }

    private fun hikari() = HikariDataSource(HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = "jdbc:postgresql://$host:$port/$dbName"
        username = dbUser
        password = dbPassword
        maximumPoolSize = 10
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })

    private fun runFlyway(datasource: DataSource) {
        val flyway = Flyway.configure()
            .dataSource(datasource)
            .load()
        try {
            flyway.info()
            flyway.migrate()
        } catch (e: Exception) {
            log.error("Exception running flyway migration", e)
            throw e
        }
        log.info("Flyway migration has finished")
    }

    suspend fun <T> dbQuery(context: CoroutineDispatcher = Dispatchers.IO, block: suspend () -> T): T =
        newSuspendedTransaction(context) { block() }
}