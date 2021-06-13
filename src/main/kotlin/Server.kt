import db.DatabaseFactory
import di.ktorModule
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin

fun Application.module() {
    // Default header for response
    install(DefaultHeaders) {
        header("Server", "Custom")
        header("X-Content-Type-Options", "nosniff")
        header("X-Frame-Options", "deny")
        header("Content-Security-Policy", "default-src 'none'")
    }
    // Logging
    install(CallLogging)
    // use kotlin serializer for json formatting
    install(ContentNegotiation) { json() }
    // Dependency injection
    install(Koin) { modules(ktorModule) }
    // config database with Hikari and Flyway version controller
    DatabaseFactory.connectAndMigrate()

    install(Routing) {
        text()
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
}

private fun Route.text() {
    get {
        call.respondText("Slice Pizza")
    }
}