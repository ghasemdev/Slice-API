package routes.validation

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import model.Error
import org.koin.ktor.ext.inject
import utils.Crypto
import utils.isEmail
import utils.isPhoneNumber
import utils.toHex

fun Route.validationRouter() {
    val validationService: ValidationService by inject()
    val crypto: Crypto by inject()

    suspend fun sendOtp(phone: String, massage: String, otp: String) {
        coroutineScope {
            validationService.apply {
                deleteExpiredOtps()
                val send = async { sendOtp(phone, massage) }
                val insert = async { insertOtp(phone = crypto.hashContent(phone).toHex(), otp = otp) }

                awaitAll(send, insert)
            }
        }
    }

    suspend fun sendOtpWithEmail(email: String, massage: String, otp: String) {
        coroutineScope {
            validationService.apply {
                deleteExpiredOtps()
                val send = async { sendOtpWithEmail(email, massage) }
                val insert = async { insertOtp(email = crypto.hashContent(email).toHex(), otp = otp) }

                awaitAll(send, insert)
            }
        }
    }

    post("/validation") {
        with(call) {
            val contentType = request.contentType()
            if (contentType == ContentType.parse("application/x-www-form-urlencoded")) {
                val body = receiveParameters()
                val phone = body["phone"]
                val email = body["email"]

                // bad request when phone and email null or empty
                if (phone.isNullOrEmpty() && email.isNullOrEmpty()) {
                    respond(HttpStatusCode.BadRequest)
                }

                // create otp
                val otp = crypto.generateOtp(length = 6)
                val otpHashed = crypto.hashContent(otp).toHex()
                val massage = "« Slice »\nYour membership verification code :\n $otp"

                // phone validation
                if (phone != null && phone.isPhoneNumber()) {
                    sendOtp(phone.trim(), massage, otpHashed)
                    respond(HttpStatusCode.OK)
                }
                // email validation
                else if (email != null && email.isEmail() && email.length <= 50) {
                    sendOtpWithEmail(email.trim(), massage, otpHashed)
                    respond(HttpStatusCode.OK)
                } else {
                    respond(HttpStatusCode.BadRequest, Error("phone number or email address isn't valid"))
                }
            } else respond(HttpStatusCode.UnsupportedMediaType)
        }
    }
}