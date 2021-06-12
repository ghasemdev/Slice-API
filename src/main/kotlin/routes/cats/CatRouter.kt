package routes.cats

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.Cat
import org.koin.ktor.ext.inject

fun Route.catRouter() {
    val catsService: CatsService by inject()

    route("/cats") {
        post { // Create Cat
            with(call) {
                val contentType = request.contentType()
                if (contentType == ContentType.parse("application/x-www-form-urlencoded")) {
                    val body = receiveParameters()
                    val name = body["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val age = body["age"]?.toInt()

                    val result = catsService.create(Cat(name = name, age = age))
                    respond(HttpStatusCode.Created, result.toString())
                } else respond(HttpStatusCode.UnsupportedMediaType)
            }
        }
        get { // Get All Cats
            call.respond(catsService.getAll())
        }
        get("/{id}") { // Search Cat
            with(call) {
                val id = parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                catsService.findById(id)?.let { cat ->
                    respond(HttpStatusCode.OK, cat)
                } ?: respond(HttpStatusCode.NotFound)
            }
        }
        delete { // Delete Cat
            with(call) {
                val contentType = request.contentType()
                if (contentType == ContentType.parse("application/x-www-form-urlencoded")) {
                    val body = receiveParameters()
                    val id = body["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

                    catsService.delete(id)
                    respond(HttpStatusCode.NoContent)
                } else respond(HttpStatusCode.UnsupportedMediaType)
            }
        }
        put {
            with(call) {
                val contentType = request.contentType()
                if (contentType == ContentType.parse("application/x-www-form-urlencoded")) {
                    val body = receiveParameters()
                    val id = body["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                    val name = body["name"]
                    val age = body["age"]?.toInt()

                    catsService.update(Cat(id, name, age))
                    respond(HttpStatusCode.OK)
                } else respond(HttpStatusCode.UnsupportedMediaType)
            }
        }
    }
}