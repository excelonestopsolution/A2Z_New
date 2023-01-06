package com.a2z.app.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a2z.app.ui.util.resource.ResultType
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream

class ApiUtil(val context: Context) {




    @Throws(Exception::class)
    suspend inline fun <reified T> loadJSONFromAsset(
        jsonFileName: String,
        delayInSecond: Int = 1,
    ): ResultType<T> {
        try {
            val data = GlobalScope.async(Dispatchers.Main) {
                delay((delayInSecond * 1000).toLong())
                val data = loadJSONFromAsset(context, jsonFileName)
                    ?: throw Exception("Fake Model is Empty!!!")

                AppUtil.logger("FAKE API CALL ::: " + T::class.simpleName)
                AppUtil.logger("=============FAKE API START===============")
                AppUtil.logger(data)
                AppUtil.logger("=============FAKE API END===============")
                Gson().fromJson(data, T::class.java)
            }
            val result =  data.await()
            return  ResultType.Success(result)

        } catch (e: java.lang.Exception) {
            throw  e
        }
    }

    fun loadJSONFromAsset(context: Context, jsonFileName: String): String? {
        val json: String? = try {
            val inputStream: InputStream =
                context.assets.open("json/" + jsonFileName.trim() + ".json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }


}