package routes.wishlist

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.ErrorResponse
import model.FoodsResponse
import model.tokenParser
import org.koin.ktor.ext.inject
import routes.validation.ValidationService
import utils.DuplicateRecordException
import utils.RecordNotExistException
import java.util.*

fun Route.wishlistRoute() {
    val validationService: ValidationService by inject()
    val wishlistService: WishlistService by inject()

    route("/wishlist") {
        authenticate {
            get {
                with(call) {
                    val token = tokenParser
                    val userId = token?.id

                    // Validate token
                    if (userId != null && validationService.validateToken(userId)) {
                        val limit = parameters["limit"]?.trim()?.toInt()
                            ?: return@get respond(HttpStatusCode.BadRequest)
                        val page = parameters["page"]?.trim()?.toLong()
                            ?: return@get respond(HttpStatusCode.BadRequest)

                        val wishlist = wishlistService.getAll(UUID.fromString(userId), limit, page)
                        respond(FoodsResponse(page, wishlist.size, wishlist))
                    } else respond(HttpStatusCode.Unauthorized, ErrorResponse("Auth token is invalid!!!"))
                }
            }

            post {
                with(call) {
                    val token = tokenParser
                    val userId = token?.id

                    // Validate token
                    if (userId != null && validationService.validateToken(userId)) {
                        val contentType = request.contentType()
                        if (contentType == ContentType.Application.FormUrlEncoded) {
                            val body = receiveParameters()
                            val foodId = body["foodId"]?.trim()?.toLong()
                                ?: return@post respond(HttpStatusCode.BadRequest)

                            addToWishlist(wishlistService, userId, foodId)
                        } else respond(HttpStatusCode.UnsupportedMediaType)
                    } else respond(HttpStatusCode.Unauthorized, ErrorResponse("Auth token is invalid!!!"))
                }
            }

            delete {
                with(call) {
                    val token = tokenParser
                    val userId = token?.id

                    // Validate token
                    if (userId != null && validationService.validateToken(userId)) {
                        val contentType = request.contentType()
                        if (contentType == ContentType.Application.FormUrlEncoded) {
                            val body = receiveParameters()
                            val foodId = body["foodId"]?.trim()?.toLong()
                                ?: return@delete respond(HttpStatusCode.BadRequest)

                            deleteFromWishlist(wishlistService, userId, foodId)
                        } else respond(HttpStatusCode.UnsupportedMediaType)
                    } else respond(HttpStatusCode.Unauthorized, ErrorResponse("Auth token is invalid!!!"))
                }
            }
        }
    }
}

private suspend fun ApplicationCall.addToWishlist(wishlistService: WishlistService, userId: String, foodId: Long) {
    try {
        wishlistService.add(UUID.fromString(userId), foodId)
        respond(HttpStatusCode.Created)
    } catch (exception: DuplicateRecordException) {
        respond(HttpStatusCode.BadRequest, ErrorResponse(exception.message!!))
    } catch (exception: RecordNotExistException) {
        respond(HttpStatusCode.BadRequest, ErrorResponse(exception.message!!))
    }
}

private suspend fun ApplicationCall.deleteFromWishlist(wishlistService: WishlistService, userId: String, foodId: Long) {
    try {
        wishlistService.delete(UUID.fromString(userId), foodId)
        respond(HttpStatusCode.Created)
    } catch (exception: RecordNotExistException) {
        respond(HttpStatusCode.BadRequest, ErrorResponse(exception.message!!))
    }
}