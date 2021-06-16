package utils

import com.google.common.truth.Truth.assertThat
import di.KoinTestExtension
import di.ktorModuleTest
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.inject

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CryptoTest : KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(ktorModuleTest)
    }

    private val crypto: Crypto by inject()

    @Test
    @Order(1)
    fun `generate otp without character`() {
        val otp = crypto.generateOtp()

        assertThat(otp.isNumber()).isTrue()
        assertThat(otp.length).isEqualTo(5)
        testLog("generate otp with out character") { otp }
    }

    @Test
    @Order(2)
    fun `generate otp`() {
        val otp = crypto.generateOtp(length = 6, isCharacterEnabled = true)

        assertThat(otp.isEnglish()).isTrue()
        assertThat(otp.length).isEqualTo(6)
        testLog("generate otp", true) { otp }
    }

    @Test
    @Order(3)
    fun `generate password without symbols`() {
        val otp = crypto.generatePassword(isSymbolsEnabled = false)

        assertThat(otp.isEnglish()).isTrue()
        assertThat(otp.length).isEqualTo(8)
        testLog("generate password with out symbols") { otp }
    }

    @Test
    @Order(4)
    fun `generate password`() {
        val otp = crypto.generatePassword()

        assertThat(otp.length).isEqualTo(8)
        testLog("generate password", true) { otp }
    }

    @Test
    @Order(5)
    fun `hash password`() {
        val hash = crypto.hashContent("1234")

        assertThat(hash.toHex()).isEqualTo("DA 4C 79 A8 FF E0 95 24 E8 E6 C3 1B C6 A1 AE 9B")
        testLog("hash password toHex") { hash.toHex() }

        assertThat(hash.toReversedHex()).isEqualTo("9B AE A1 C6 1B C3 E6 E8 24 95 E0 FF A8 79 4C DA")
        testLog("hash password toReversedHex") { hash.toReversedHex() }

        assertThat(hash.toDec()).isEqualTo("218761211682552241493623223019527198161174155")
        testLog("hash password toDec") { hash.toDec() }

        assertThat(hash.toReversedDec()).isEqualTo("155174161198271952302323614922425516812176218")
        testLog("hash password toReversedDec", true) { hash.toReversedDec() }
    }

    @Test
    @Order(6)
    fun cryptography() {
        val text = "hi my name is jakode"
        testLog("cryptography text") { text }

        val encrypt = crypto.encrypt(text)
        testLog("cryptography encrypt", true) { encrypt }

        val decrypt = crypto.decrypt(encrypt)
        assertThat(decrypt).isEqualTo(text)
    }

    @Test
    @Order(7)
    fun `length hashes and encrypted content`() {
        val hashedPhone = crypto.hashContent("09152165050").toHex()
        val hashedEmail = crypto.hashContent("shirdelghasem79@gmail.com").toHex()
        testLog("hashedPhone length") { hashedPhone.length }
        testLog("hashedEmail length") { hashedEmail.length }

        assertThat(hashedPhone.length).isEqualTo(47)
        assertThat(hashedEmail.length).isEqualTo(47)

        val phoneEncrypt = crypto.encrypt("09309275920")
        val emailEncrypt = crypto.encrypt("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@gmail.com")
        testLog("phoneEncrypt length") { phoneEncrypt.length }
        testLog("emailEncrypt length", true) { emailEncrypt.length }

        assertThat(phoneEncrypt.length).isEqualTo(24)
        assertThat(emailEncrypt.length).isEqualTo(88)
    }

    @Test
    @Order(8)
    fun `Url encoding`() {
        val originalUrl = "https://www.google.co.nz/?gfe_rd=cr&ei=dzbFV&gws_rd=ssl#q=java"
        val encodingUrl = crypto.urlEncoding(originalUrl)
        testLog("encoding url") { encodingUrl }
        assertThat(encodingUrl).isEqualTo("aHR0cHM6Ly93d3cuZ29vZ2xlLmNvLm56Lz9nZmVfcmQ9Y3ImZWk9ZHpiRlYmZ3dzX3JkPXNzbCNxPWphdmE=")

        val decodedUrl = crypto.urlDecoding(encodingUrl)
        testLog("decoded url") { decodedUrl }
        assertThat(decodedUrl).isEqualTo(originalUrl)
    }
}