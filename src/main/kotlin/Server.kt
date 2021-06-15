import db.DatabaseFactory
import di.ktorModule
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import model.Email
import model.Phone
import model.PrincipalJwt
import org.koin.ktor.ext.Koin
import routes.files.pictures.pictureRouter
import routes.user.userRouter
import routes.validation.validationRouter
import utils.JwtConfig

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
    // JWT
    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = "slice api"
            validate {
                val name = it.payload.getClaim("name").asString()
                val phone = it.payload.getClaim("phone").asString()
                val email = it.payload.getClaim("email").asString()

                if (name != null && (phone != null || email != null)) {
                    PrincipalJwt(name, Phone(phone), Email(email))
                } else {
                    null
                }
            }
        }
    }

    install(Routing) {
        validationRouter()
        userRouter()
        pictureRouter()
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
}