package routes.validation

import com.kavenegar.sdk.KavenegarApi
import com.kavenegar.sdk.excepctions.ApiException
import com.kavenegar.sdk.excepctions.HttpException
import db.DatabaseFactory.dbQuery
import db.table.OtpsTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import model.Email
import model.EmailServer
import model.Phone
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ValidationServiceImp constructor(
    private val apiKey: String,
    private val emailServer: EmailServer,
    private val expiredTime: Duration
) : ValidationService {

    override suspend fun sendOtp(phone: Phone, massage: String): Unit = coroutineScope {
        withContext(Dispatchers.Default) {
            try {
                val api = KavenegarApi(apiKey)
                api.send("1000596446", phone.value, massage)
            } catch (exception: HttpException) {
                print("HttpException  : " + exception.message)
            } catch (exception: ApiException) {
                print("ApiException : " + exception.message)
            }
        }
    }

    override suspend fun sendOtpWithEmail(email: Email, massage: String): Unit = coroutineScope {
        withContext(Dispatchers.Default) {
            SimpleEmail().apply {
                hostName = "smtp.gmail.com"
                setSmtpPort(465)
                setAuthenticator(DefaultAuthenticator(emailServer.username, emailServer.password))
                isSSLOnConnect = true
                setFrom(emailServer.email)
                subject = "Validation"
                setMsg(massage)
                addTo(email.value)
                send()
            }
        }
    }

    override suspend fun insertOtp(phone: Phone?, email: Email?, otp: String): Unit = dbQuery {
        OtpsTable.insert { otpTable ->
            phone?.let { otpTable[OtpsTable.phone] = phone.value }
            email?.let { otpTable[OtpsTable.email] = email.value }
            otpTable[OtpsTable.otp] = otp
        }
    }

    override suspend fun deleteExpiredOtps(): Unit = dbQuery {
        val createAt = LocalDateTime.now().minusMinutes(expiredTime.inWholeMinutes)
        OtpsTable.deleteWhere { OtpsTable.createAt lessEq createAt }
    }

    override suspend fun validateOtp(phone: Phone?, email: Email?, otp: String): Boolean = dbQuery {
        val createAt = LocalDateTime.now().minusMinutes(expiredTime.inWholeMinutes)
        val query = OtpsTable.select { OtpsTable.otp eq otp and (OtpsTable.createAt greater createAt) }

        if (phone != null) {
            query.andWhere { OtpsTable.phone eq phone.value }.firstOrNull()?.let {
                deleteOtp(it[OtpsTable.id].value)
                return@dbQuery true
            }
        } else if (email != null) {
            query.andWhere { OtpsTable.email eq email.value }.firstOrNull()?.let {
                deleteOtp(it[OtpsTable.id].value)
                return@dbQuery true
            }
        }
        false
    }

    private suspend fun deleteOtp(id: UUID) {
        dbQuery { OtpsTable.deleteWhere { OtpsTable.id eq id } }
    }
}