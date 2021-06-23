package routes.foods

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import org.jetbrains.exposed.sql.SortOrder
import org.koin.ktor.ext.inject
import routes.validation.ValidationService
import utils.PreparationTime
import utils.Price
import utils.Score

fun Route.foodsRoute() {
    val validationService: ValidationService by inject()
    val foodsService: FoodsService by inject()

    route("/foods") {
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
                        val category = parameters["category"]?.let { FoodCategory.getEnum(it) }

                        val search = parameters["search"]?.trim()
                        val isActive = parameters["isActive"]?.let { it.trim().toBoolean() }
                        val size = parameters["size"]?.let { FoodSize.getEnum(it) }
                        val price = parameters["price"]?.let { price ->
                            val (from, to) = price.trim().split("-").map { it.toFloat() }
                            Price(from, to)
                        }
                        val score = parameters["score"]?.let { score ->
                            val (from, to) = score.trim().split("-").map { it.toFloat() }
                            Score(from, to)
                        }
                        val time = parameters["time"]?.let { preparationTime ->
                            val (from, to) = preparationTime.trim().split("-").map { it.toInt() }
                            PreparationTime(from, to)
                        }
                        val order = parameters["order"]?.let { order ->
                            val (orderBy, sortOrder) = order.trim().split("-")
                            Order(OrderBy.getEnum(orderBy), SortOrder.valueOf(sortOrder.uppercase()))
                        }

                        val foods = foodsService.getAll(
                            FoodsFilter(limit, page, category, search, isActive, size, price, score, time, order)
                        )
                        respond(FoodsResponse(page, foods.size, foods))
                    } else respond(HttpStatusCode.Unauthorized, ErrorResponse("Auth token is invalid!!!"))
                }
            }

            get("/special-offer") {
                with(call) {
                    val token = tokenParser
                    val userId = token?.id

                    // Validate token
                    if (userId != null && validationService.validateToken(userId)) {
                        val limit = parameters["limit"]?.trim()?.toInt()
                            ?: return@get respond(HttpStatusCode.BadRequest)
                        val page = parameters["page"]?.trim()?.toLong()
                            ?: return@get respond(HttpStatusCode.BadRequest)

                        val specialOffer = foodsService.getSpecialOffer(limit, page)
                        respond(FoodsResponse(page, specialOffer.size, specialOffer))
                    } else respond(HttpStatusCode.Unauthorized, ErrorResponse("Auth token is invalid!!!"))
                }
            }

            get("/best-selling") {
                with(call) {
                    val token = tokenParser
                    val userId = token?.id

                    // Validate token
                    if (userId != null && validationService.validateToken(userId)) {
                        val limit = parameters["limit"]?.trim()?.toInt()
                            ?: return@get respond(HttpStatusCode.BadRequest)
                        val page = parameters["page"]?.trim()?.toLong()
                            ?: return@get respond(HttpStatusCode.BadRequest)

                        val bestSelling = foodsService.getBestSelling(limit, page)
                        respond(FoodsResponse(page, bestSelling.size, bestSelling))
                    } else respond(HttpStatusCode.Unauthorized, ErrorResponse("Auth token is invalid!!!"))
                }
            }

            get("/highest-scores") {
                with(call) {
                    val token = tokenParser
                    val userId = token?.id

                    // Validate token
                    if (userId != null && validationService.validateToken(userId)) {
                        val limit = parameters["limit"]?.trim()?.toInt()
                            ?: return@get respond(HttpStatusCode.BadRequest)
                        val page = parameters["page"]?.trim()?.toLong()
                            ?: return@get respond(HttpStatusCode.BadRequest)

                        val highestScores = foodsService.getHighestScores(limit, page)
                        respond(FoodsResponse(page, highestScores.size, highestScores))
                    } else respond(HttpStatusCode.Unauthorized, ErrorResponse("Auth token is invalid!!!"))
                }
            }
        }

        get("/{id}") {

        }
    }
}