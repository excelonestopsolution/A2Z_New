package com.a2z.app.util

import android.util.Log
import com.a2z.app.data.local.AppPreference
import org.json.JSONObject
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



    fun urlToJson(url: String?): JSONObject? {
        if(url== null) return null
        return try {
            val map: MutableMap<String?, Any?> = HashMap()
            val hashes = url.slice((url.indexOf("?") + 1)..url.lastIndex).split("&")
            for (hash in hashes) {
                val subHashes = hash.split("=")

                map[subHashes[0]] = subHashes[1]
            }
            JSONObject(map)
        }catch (e : java.lang.Exception) {
            null
        }

    }


}