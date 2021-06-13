package routes.sms

interface OtpService {
    suspend fun sendOtp(number: String, massage: String)
    suspend fun sendOtpWithEmail(email: String, massage: String)
    suspend fun insertOtp(otp: String)
    suspend fun deleteExpiredOtps()
    suspend fun validateOtp(otp: String): Boolean
}