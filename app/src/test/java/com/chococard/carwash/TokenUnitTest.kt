package com.chococard.carwash

import org.junit.Test
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class TokenUnitTest {

    @Test
    fun tokenTest() {

        val data = "pathiphon"
//        val shaData = encryptSHA(data)

        println("data : $data")
//        println("shaData toString : $shaData")

        val encodedString: String = data.base64Encode() // MTIzNDU2
        println("BASE64 encode : $encodedString")

        val decodedString = encodedString.base64Decode()
        println("BASE64 decode : $decodedString")

    }

    fun String.base64Encode(): String {
        if (this.isEmpty()) return ""
        val byteArray = this.toByteArray()
        val baseEncode = Base64.getEncoder()
        val encode = baseEncode.encodeToString(byteArray)
        val otherEncode = encode.substring(1)
        val firstEncode = encode.substring(0, 1)
        return otherEncode + firstEncode
    }

    fun String.base64Decode(): String {
        if (this.isEmpty()) return ""
        val firstDecode = this.substring(this.length - 1, this.length)
        val otherDecode = this.substring(0, this.length - 1)
        val decode = firstDecode + otherDecode
        val baseDecode = Base64.getDecoder()
        val byteArray = baseDecode.decode(decode)
        return String(byteArray)
    }

    fun encryptSHA(data: String): String {
        var sha = ""
        try {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            val byteArray = messageDigest.digest(data.toByteArray())
            val bigInteger = BigInteger(1, byteArray)
            sha = bigInteger.toString(16)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return sha
    }

}
