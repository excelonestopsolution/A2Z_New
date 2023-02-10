package com.a2z.app.util

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AppSecurity {
    private const val ALGORITHM = "AES"
    private const val SALT_AND_ENCRYPT_KEY = "OPENUSERIDPASSWO"

    fun encrypt(input: String): String {
        return try {
            val secretKeySpec = SecretKeySpec(SALT_AND_ENCRYPT_KEY.toByteArray(), ALGORITHM)
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            val encryptedData = cipher.doFinal(input.toByteArray())
            String(Base64.encode(encryptedData, Base64.DEFAULT), Charsets.UTF_8)
        } catch (e: Exception) {
            ""
        }
    }


    fun decrypt(input: String): String {
        return try {
            val secretKeySpec = SecretKeySpec(SALT_AND_ENCRYPT_KEY.toByteArray(), ALGORITHM)
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            val mValue: ByteArray = Base64.decode(input, Base64.DEFAULT)
            val result = cipher.doFinal(mValue)
            String(result, Charsets.UTF_8)

        } catch (e: Exception) {
            ""
        }
    }
}