package routes.sms

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.koin.ktor.ext.inject
import utils.Crypto
import utils.toHex

fun Route.smsRouter() {
    val otpService: OtpService by inject()
    val crypto: Crypto by inject()

    route("/otp") {
        post {
            with(call) {
                val contentType = request.contentType()
                if (contentType == ContentType.parse("application/x-www-form-urlencoded")) {
                    val body = receiveParameters()
                    val phone = body["phone"] ?: return@post respond(HttpStatusCode.BadRequest)

                    val otp = crypto.generateOtp(length = 6)
                    val otpHashed = crypto.hashContent(otp).toHex()

                    val massage = "« Slice »\nYour membership verification code :\n $otp"
                    otpService.apply {
                        deleteExpiredOtps()
                        val send = async { sendOtp(phone, massage) }
                        val insert = async { insertOtp(otpHashed) }

                        awaitAll(send, insert)
                    }

                    respond(HttpStatusCode.OK)
                } else respond(HttpStatusCode.UnsupportedMediaType)
            }
        }
    }
}