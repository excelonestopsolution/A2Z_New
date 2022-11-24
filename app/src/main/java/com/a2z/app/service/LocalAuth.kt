package com.a2z.app.service

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity

object LocalAuth {

    fun checkForBiometrics(context: Context): Boolean {
        val status = BiometricManager.from(context).canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        return status == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun showBiometricPrompt(
        activity: FragmentActivity,
        callback: (LocalAuthResultType<BiometricPrompt.AuthenticationResult>) -> Unit
    ) {


        // 2
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Use biometric")
            .setSubtitle("No need to login again and again")
            .setConfirmationRequired(true)
            .setDeviceCredentialAllowed(true)
            .build()

        // 3
        val biometricPrompt = BiometricPrompt(
            activity,
            object : BiometricPrompt.AuthenticationCallback() {
                // 4
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    callback.invoke(LocalAuthResultType.Error(errorCode, errString.toString()))
                }

                // 5
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                  callback.invoke(LocalAuthResultType.Success(result))
                }

                // 6
                override fun onAuthenticationFailed() {
                    callback.invoke(LocalAuthResultType.Failure)
                }
            }
        )
        // 7
        biometricPrompt.authenticate(promptInfo)
    }
}

sealed class LocalAuthResultType<out T> {
    class Success<out T>(val result: BiometricPrompt.AuthenticationResult) :
        LocalAuthResultType<T>()

    object Failure : LocalAuthResultType<Nothing>()

    class Error<out T>(val errorCode: Int, val errorMessage: String) :
        LocalAuthResultType<T>()
}