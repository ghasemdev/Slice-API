package routes.validation

import model.Email
import model.Phone

interface ValidationService {
    suspend fun sendOtp(phone: Phone, massage: String)
    suspend fun sendOtpWithEmail(email: Email, massage: String)
    suspend fun insertOtp(phone: Phone? = null, email: Email? = null, otp: String)
    suspend fun validateOtp(phone: Phone? = null, email: Email? = null, otp: String): Boolean
    suspend fun deleteExpiredOtps()
}