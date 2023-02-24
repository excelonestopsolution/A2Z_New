package com.a2z.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import com.a2z.app.data.local.AppPreference
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
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
        if (url == null) return null
        return try {
            val map: MutableMap<String?, Any?> = HashMap()
            val hashes = url.slice((url.indexOf("?") + 1)..url.lastIndex).split("&")
            for (hash in hashes) {
                val subHashes = hash.split("=")

                map[subHashes[0]] = subHashes[1]
            }
            JSONObject(map)
        } catch (e: java.lang.Exception) {
            null
        }

    }


    @SuppressLint("Range")
    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme.equals("content")) {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex("_data")
                    result = cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                if (cut != null) {
                    result = result?.substring(cut + 1)
                }
            }
        }
        return result
    }


}