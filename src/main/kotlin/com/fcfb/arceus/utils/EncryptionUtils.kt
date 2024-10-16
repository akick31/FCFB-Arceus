package com.fcfb.arceus.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class EncryptionUtils {
    @Value("\${encryption.algorithm}")
    private val algorithm: String? = null

    @Value("\${encryption.key}")
    private val encryptionKey: String? = null

    @Throws(Exception::class)
    fun encrypt(value: String): String {
        val secretKey: SecretKey = SecretKeySpec(encryptionKey!!.toByteArray(), algorithm)
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(value.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    @Throws(Exception::class)
    fun decrypt(encryptedValue: String?): String {
        val secretKey: SecretKey = SecretKeySpec(encryptionKey!!.toByteArray(), algorithm)
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue))
        return String(decryptedBytes, StandardCharsets.UTF_8)
    }
}
