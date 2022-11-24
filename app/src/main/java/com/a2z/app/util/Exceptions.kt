package com.a2z.app.util

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.io.IOException

@Parcelize
@Keep
data class AppException(
     val exception : Exception,
) : Parcelable

object Exceptions {

     class NoInternetException(message: String) : IOException(message)
     class UnAuthorizedException(message : String) : IOException(message)
     class InternalServerError(message : String) : IOException(message)
     class RootException() : IOException("Rooted Device Found!")
     class SessionExpiredException(message: String) : IOException(message)
     class AppInProgressException(message: String) : IOException(message)
     class GeneralException(message : String = "Something went wrong! client side") : IOException(message)
     class DoubleTransactionException(message : String) : IOException(message)
}


