package routes.validation

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import model.Email
import model.ErrorResponse
import model.Phone
import org.koin.ktor.ext.inject
import utils.Crypto
import utils.toHex

fun Route.validationRouter() {
    val validationService: ValidationService by inject()
    val crypto: Crypto by inject()

    suspend fun sendOtp(phone: String, massage: String, otp: String) {
        coroutineScope {
            validationService.apply {
                deleteExpiredOtps()
                val send = async { sendOtp(Phone(phone), massage) }
                val insert = async { insertOtp(phone = Phone(crypto.hashContent(phone).toHex()), otp = otp) }

                awaitAll(send, insert)
            }
        }
    }

    suspend fun sendOtpWithEmail(email: String, massage: String, otp: String) {
        coroutineScope {
            validationService.apply {
                deleteExpiredOtps()
                val send = async { sendOtp(Email(email), massage) }
                val insert = async { insertOtp(email = Email(crypto.hashContent(email).toHex()), otp = otp) }

                awaitAll(send, insert)
            }
        }
    }

    post("/account/validation") {
        with(call) {
            val contentType = request.contentType()
            if (contentType == ContentType.Application.FormUrlEncoded) {
                val body = receiveParameters()
                val phone = body["phone"]?.trim()
                val email = body["email"]?.trim()

                // bad request when phone and email null or empty
                if (phone.isNullOrEmpty() && email.isNullOrEmpty()) {
                    respond(HttpStatusCode.BadRequest, ErrorResponse("phone number or email address is empty or null"))
                }

                // create otp
                val otp = crypto.generateOtp(length = 6)
                val otpHashed = crypto.hashContent(otp).toHex()
                val massage = "« Slice »\nYour membership verification code :\n $otp"

                // phone validation
                if (phone != null && Phone.isValid(phone)) {
                    sendOtp(phone, massage, otpHashed)
                    respond(HttpStatusCode.NoContent)
                }
                // email validation
                else if (email != null && Email.isValid(email) && email.length <= 50) {
                    sendOtpWithEmail(email, massage, otpHashed)
                    respond(HttpStatusCode.NoContent)
                } else {
                    respond(HttpStatusCode.BadRequest, ErrorResponse("phone number or email address isn't valid"))
                }
            } else respond(HttpStatusCode.UnsupportedMediaType)
        }
    }
}