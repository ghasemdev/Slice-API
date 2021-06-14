package utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

// const val
const val PASSWORD_HASH_ALGORITHM_PBKDF2_HMAC_SHA512 = "PBKDF2WithHmacSHA512"
const val PASSWORD_HASH_ITERATIONS = 65536
const val PASSWORD_HASH_KEY_SIZE = 128

val DateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
val TimeFormatter = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)

/** check is a english string*/
fun String.isEnglish() = this.matches(Regex("\\w*"))

/** check is a number*/
fun String.isNumber() = this.matches(Regex("\\d*"))

/** check is a phone number*/
fun String.isPhoneNumber() = this.isNotEmpty() && this.trim().matches(Regex("09\\d{9}"))

/** check is a valid email*/
fun String.isEmail() = this.isNotEmpty() && this.trim().matches(Regex("^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$"))

/** byte array to hex*/
fun ByteArray.toHex() = this.joinToString(" ") { "%02X".format(it) }

/** byte array to reversed hex*/
fun ByteArray.toReversedHex() = this.reversedArray().toHex()

/** byte array to decimal*/
fun ByteArray.toDec() = buildString { this@toDec.forEach { append("%02X".format(it).toLong(16)) } }

/** byte array to reversed decimal*/
fun ByteArray.toReversedDec() = this.reversedArray().toDec()

fun StringBuilder.space(): StringBuilder = append(" ")

/**
 * log a message for tests
 *
 * @param methodName from rule name
 * @param massage
 */
fun testLog(methodName: String, isDoubleNewLine: Boolean = true, massage: () -> String) {
    println("$methodName : ${massage()}${if (isDoubleNewLine) "\n" else ""}")
}

fun nanoTime(function: () -> Unit) {
    val start = System.nanoTime()
    function()
    println(System.nanoTime() - start)
}

/**
 * Refresh a list with clearing and add items
 * @param items
 * @return array list
 */
fun <T : Any> ArrayList<T>.refreshList(items: List<T>): ArrayList<T> {
    this.clear()
    this.addAll(items)
    return this
}

/**
 * Add new items to list
 * @param items
 * @return array list
 */
fun <T : Any> ArrayList<T>.addOnlyNewItems(items: List<T>): ArrayList<T> {
    items.forEach { if (!this.contains(it)) this.add(it) }
    return this
}

/**
 * Delete decimal part when float number has .0
 * @return string
 */
fun Float.deleteDecimalPart(): String {
    val numberToInt = this.toInt()
    return if (this == numberToInt.toFloat()) numberToInt.toString() else this.toString()
}

/**
 * Separate number every 3 digits
 * @return string
 */
fun Double.formatWithCommas(): String {
    val df = DecimalFormat("###,###,###")
    return df.format(this)
}