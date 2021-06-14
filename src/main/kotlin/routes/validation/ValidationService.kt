package routes.validation

interface ValidationService {
    suspend fun sendOtp(number: String, massage: String)
    suspend fun sendOtpWithEmail(email: String, massage: String)
    suspend fun insertOtp(phone: String? = null, email: String? = null, otp: String)
    suspend fun deleteExpiredOtps()
//    suspend fun validateOtp(otp: String): Boolean
}