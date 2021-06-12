import database.DatabaseFactory
import di.ktorModule
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.ext.Koin
import utils.flyway.FlywayFeature

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, module = Application::mainModule).run {
        start(wait = true)
    }
}

fun Application.mainModule() {
    // Default header for response
    install(DefaultHeaders) {
        header(HttpHeaders.Server, "Custom")
    }
    // use kotlin serializer for json formatting
    install(ContentNegotiation) { json() }
    // Dependency injection
    install(Koin) { modules(ktorModule) }
    // config database with Hikari and Flyway version controller
    val database = DatabaseFactory.connect()
    Database.connect(database)
    install(FlywayFeature) {
        dataSource = database
        locations = arrayOf("database/migration")
    }

    routing {
        // log request
        trace { application.log.debug(it.buildText()) }
        mainRouter()
    }
}

private fun Routing.mainRouter() {
    get {
        call.respondText("Slice Pizza")
    }
}