package routes.validation

import kpy.struct.Email
import kpy.struct.Phone

interface ValidationService {
    suspend fun sendOtp(phone: Phone, massage: String)
    suspend fun sendOtp(email: Email, massage: String)
    suspend fun insertOtp(phone: Phone? = null, email: Email? = null, otp: String)
    suspend fun validateOtp(phone: Phone? = null, email: Email? = null, otp: String): Boolean
    suspend fun validateToken(userId: String): Boolean
    suspend fun deleteExpiredOtps()
}