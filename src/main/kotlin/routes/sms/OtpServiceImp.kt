package routes.sms

import com.kavenegar.sdk.KavenegarApi
import com.kavenegar.sdk.excepctions.ApiException
import com.kavenegar.sdk.excepctions.HttpException
import database.DatabaseFactory.dbQuery
import database.table.Otps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import model.Email
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class OtpServiceImp constructor(
    private val apiKey: String,
    private val emailServer: Email,
    private val expiredTime: Duration
) : OtpService {

    override suspend fun sendOtp(number: String, massage: String): Unit = coroutineScope {
        withContext(Dispatchers.Default) {
            try {
                val api = KavenegarApi(apiKey)
                api.send("1000596446", number, massage)
            } catch (exception: HttpException) {
                print("HttpException  : " + exception.message)
            } catch (exception: ApiException) {
                print("ApiException : " + exception.message)
            }
        }
    }

    override suspend fun sendOtpWithEmail(email: String, massage: String): Unit = coroutineScope {
        withContext(Dispatchers.Default) {
            SimpleEmail().apply {
                hostName = "smtp.gmail.com"
                setSmtpPort(465)
                setAuthenticator(DefaultAuthenticator(emailServer.username, emailServer.password))
                isSSLOnConnect = true
                setFrom(emailServer.email)
                subject = "Validation"
                setMsg(massage)
                addTo(email)
                send()
            }
        }
    }

    override suspend fun insertOtp(otp: String): Long = dbQuery {
        Otps.insertAndGetId { it[Otps.otp] = otp }.value
    }

    override suspend fun deleteExpiredOtps() {
        dbQuery {
            val createAt = LocalDateTime.now().minusSeconds(expiredTime.inWholeSeconds)
            Otps.deleteWhere { Otps.create_at lessEq createAt }
        }
    }

    override suspend fun validateOtp(otp: String): Boolean = dbQuery {
        val createAt = LocalDateTime.now().minusSeconds(expiredTime.inWholeSeconds)
        Otps.select { Otps.otp eq otp and (Otps.create_at greater createAt) }.firstOrNull()?.let {
            deleteOtp(it[Otps.id].value)
            true
        } ?: false
    }

    private suspend fun deleteOtp(id: Long) {
        dbQuery { Otps.deleteWhere { Otps.id eq id } }
    }
}