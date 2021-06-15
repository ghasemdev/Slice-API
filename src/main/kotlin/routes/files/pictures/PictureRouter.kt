package routes.files.pictures

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun Route.pictureRouter() {
    route("/files/pictures/{type}/") {
        put {
            with(call) {
                val multipartData = receiveMultipart()
                val id = parameters["id"]
                val type = parameters["type"]
                var fileName = ""

                val mp = try {
                    multipartData.forEachPart { part ->
                        when (part) {
                            is PartData.FileItem -> {
                                fileName = part.originalFileName as String
                                val fileBytes = part.streamProvider().readBytes()
                                File(fileName).also { it.writeBytes(fileBytes) }
                            }
                            else -> application.log.info("Not a file item: ${part.name}")
                        }
                    }
                } catch (exception: Exception) {
                    application.log.error("Getting multipart error", exception)
                    null
                }
                mp?.let {respondText("$fileName is uploaded") } ?: respond(HttpStatusCode.BadRequest)
            }
        }

        get {
            with(call) {
                val file = File("photo_2019-05-23_20-16-05.jpg")
                response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(
                        ContentDisposition.Parameters.FileName,
                        "photo_2019-05-23_20-16-05.jpg"
                    ).toString()
                )
                respondFile(file)
            }
        }
    }
}