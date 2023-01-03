package com.a2z.app.util

import android.util.Log
import com.a2z.app.data.local.AppPreference
import java.util.*
import javax.inject.Inject


class AppUtilDI @Inject constructor(
    private val appPreference: AppPreference
) {


    fun appUniqueIdentifier(): String {
        if (appPreference.appUniqueKey.isEmpty()) {
            val uk = AppUtil.appUniqueIdentifier()
            appPreference.appUniqueKey = uk
        }
        return appPreference.appUniqueKey
    }
}


object AppUtil {


    const val Empty = ""

    @Synchronized
    fun appUniqueIdentifier(): String {
        return UUID.randomUUID().toString()
    }

    fun logger(message: Any, tag: String = "AppLog") {
        Log.d(tag, message.toString())
    }




}