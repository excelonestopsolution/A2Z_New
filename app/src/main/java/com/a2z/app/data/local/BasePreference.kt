package com.a2z.app.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.a2z.app.util.security.AppSecurity
import com.google.gson.Gson

open class BasePreference(context: Context) {
    companion object {
        const val UNIQUE_KEY = "app_unique_key"
        const val USER = "user"
        const val UPI_STATIC_MESSAGE = "upi_static_message"
        const val PASSWORD = "password"
        const val LOGIN_ID = "login_id"
        const val LOGIN_CHECK = "login_check"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val LOCATION_FETCHED = "location_fetched"
    }

    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences?.getBoolean(key,false) ?: false
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences?.edit()?.putBoolean(key, value)?.commit()
    }


    protected fun getString(key: String): String {
        val enValue = sharedPreferences?.getString(key, "") ?: ""
        if (enValue.isEmpty()) return ""
        return AppSecurity.decrypt(enValue) ?: ""
    }

    protected fun saveString(key: String, value: String?) {
        var storeData = value ?: ""
        if (storeData.isNotBlank()) {
            storeData = AppSecurity.encrypt(storeData) ?: ""
        }
        sharedPreferences?.edit()?.putString(key, storeData)?.commit()
    }

    protected fun <T> getObject(key: String, classJava: Class<T>): T? {
        val strObject = getString(key) ?: ""
        if (strObject.isEmpty()) return null
        return Gson().fromJson(strObject, classJava)
    }

    protected fun <T> saveObject(key: String, value: T?) {
        val storeValue = if (value == null) "" else Gson().toJson(value)
        saveString(key, storeValue)
    }
}