package routes.files.pictures

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kpy.util.crypto.Crypto
import model.ErrorResponse
import model.tokenParser
import org.koin.ktor.ext.inject
import routes.validation.ValidationService

private const val PROFILE = "profile"

fun Route.pictureRoute() {
    val pictureService: PictureService by inject()
    val validationService: ValidationService by inject()
    val crypto: Crypto by inject()

    route("/files/pictures/{type}") {
        authenticate {
            put {
                with(call) {
                    val token = tokenParser
                    val userId = token?.id

                    // Validate token
                    if (userId != null && validationService.validateToken(userId)) {
                        val type = parameters["type"]?.trim() ?: return@put respond(HttpStatusCode.BadRequest)

                        // Validate path
                        if (type == PROFILE) {
                            val multipartData = receiveMultipart()
                            var fileName = ""

                            val mp = try {
                                multipartData.forEachPart { part ->
                                    when (part) {
                                        is PartData.FileItem -> {
                                            fileName = part.originalFileName as String
                                            val fileBytes = part.streamProvider().readBytes()
                                            pictureService.updateProfile(userId, fileBytes)
                                        }
                                        else -> application.log.info("Not a file item: ${part.name}")
                                    }
                                }
                            } catch (exception: Exception) {
                                application.log.error("Getting multipart error", exception)
                                null
                            }
                            mp?.let { respondText("$fileName is uploaded") } ?: respond(HttpStatusCode.BadRequest)
                        } else respond(HttpStatusCode.NotFound, ErrorResponse("this path not exist!!!"))
                    } else respond(HttpStatusCode.Unauthorized, ErrorResponse("Auth token is invalid!!!"))
                }
            }
        }

        get("/{path}") {
            with(call) {
                val type = parameters["type"]?.trim() ?: return@get respond(HttpStatusCode.BadRequest)
                val path = parameters["path"]?.trim() ?: return@get respond(HttpStatusCode.BadRequest)

                if (type == PROFILE) {
                    val userId = crypto.urlDecoding(path)
                    val file = pictureService.getProfile(userId)
                    file?.let { respondFile(it) } ?: respond(HttpStatusCode.NotFound)
                } else respond(HttpStatusCode.NotFound, ErrorResponse("this path not exist!!!"))
            }
        }
    }
}