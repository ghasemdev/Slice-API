package routes.user

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import org.koin.ktor.ext.inject
import routes.validation.ValidationService
import utils.Crypto
import utils.toHex

fun Route.userRouter() {
    val validationService: ValidationService by inject()
    val userService: UserService by inject()
    val crypto: Crypto by inject()

    suspend fun ApplicationCall.createUserWithValidation(
        phone: Phone? = null,
        email: Email? = null,
        nickname: String,
        otp: String
    ) {
        val hashedOtp = crypto.hashContent(otp).toHex()
        if (phone != null) {
            val hashedPhone = Phone(crypto.hashContent(phone.value).toHex())

            if (validationService.validateOtp(phone = hashedPhone, otp = hashedOtp)) {
                when (val respond =
                    userService.create(User(nickname = nickname, phone = Phone(crypto.encrypt(phone.value))))) {
                    is UserResponse -> respond(HttpStatusCode.Created, respond)
                    is ErrorResponse -> respond(HttpStatusCode.BadRequest, respond)
                }
            }
        } else if (email != null) {
            val hashedEmail = Email(crypto.hashContent(email.value).toHex())

            if (validationService.validateOtp(email = hashedEmail, otp = hashedOtp)) {
                when (val respond =
                    userService.create(User(nickname = nickname, email = Email(crypto.encrypt(email.value))))) {
                    is UserResponse -> respond(HttpStatusCode.Created, respond)
                    is ErrorResponse -> respond(HttpStatusCode.BadRequest, respond)
                }
            }
        }
        respond(HttpStatusCode.Unauthorized, ErrorResponse("verify code incorrect!!!"))
    }

    route("/account") {
        post("/register") {
            with(call) {
                val contentType = request.contentType()
                if (contentType == ContentType.Application.FormUrlEncoded) {
                    val body = receiveParameters()
                    val nickname = body["nickname"]?.trim() ?: return@post respond(HttpStatusCode.BadRequest)
                    val otp = body["verificationCode"]?.trim() ?: return@post respond(HttpStatusCode.BadRequest)
                    val phone = body["phone"]?.trim()
                    val email = body["email"]?.trim()

                    // nickname and otp validation
                    if (nickname.isNotEmpty() && nickname.length <= 50 && otp.isNotEmpty() && otp.length == 6) {
                        // bad request when phone and email null or empty
                        if (phone.isNullOrEmpty() && email.isNullOrEmpty()) {
                            respond(
                                HttpStatusCode.BadRequest,
                                ErrorResponse("phone number or email address is empty or null")
                            )
                        }

                        // phone validation
                        if (phone != null && Phone(phone).isValid()) {
                            createUserWithValidation(phone = Phone(phone), nickname = nickname, otp = otp)
                        }
                        // email validation
                        else if (email != null && Email(email).isValid() && email.length <= 50) {
                            createUserWithValidation(email = Email(email), nickname = nickname, otp = otp)
                        } else {
                            respond(
                                HttpStatusCode.BadRequest,
                                ErrorResponse("phone number or email address isn't valid")
                            )
                        }
                    } else respond(HttpStatusCode.BadRequest)
                } else respond(HttpStatusCode.UnsupportedMediaType)
            }
        }
    }
}