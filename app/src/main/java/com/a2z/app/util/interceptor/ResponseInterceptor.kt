package com.a2z.app.util.interceptor

import com.a2z.app.data.local.AppPreference
import com.a2z.app.util.AppUtil
import com.a2z.app.util.Exceptions
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.Buffer
import okio.GzipSource
import org.json.JSONObject
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import javax.inject.Inject


class ResponseInterceptor @Inject constructor(
    private val appPreference: AppPreference
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {


        val request = chain.request()
        val response = chain.proceed(request)



        if (response.code == 200 || response.code == 201) {
            val jsonObject = JSONObject(cloneResponseBody(response))
            try {
                val status = jsonObject.optInt("status")
                val message = jsonObject.optString("message")
                if (status == 300 || status == 401 ) {
                    appPreference.user = null
                    throw Exceptions.SessionExpiredException(message)
                }
                if (status == 200) throw Exceptions.AppInProgressException(message)
            } catch (e: Exception) {
                if (e is Exceptions.SessionExpiredException || e is Exceptions.AppInProgressException) {
                    throw e
                }
            }
        }
        return response
    }

    private fun cloneResponseBody(response: Response): String {
        val responseBody = response.body!!
        val contentLength = responseBody.contentLength()
        val source = responseBody.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer.clone()

        val contentType = responseBody.contentType()

        return buffer.asResponseBody(
            contentType,
            Int.MAX_VALUE.toLong()
        ).string()
    }

    /*  private fun cloneResponseBody(rawResponse: Response): String {

          val responseBody = rawResponse.body
          val bufferClone: Buffer = responseBody!!.source().buffer.clone()
          return bufferClone.asResponseBody(
              responseBody.contentType(),
              responseBody.contentLength()
          ).string()
      }*/
}