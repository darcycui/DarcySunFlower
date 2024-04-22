package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.config.CALL_TIMEOUT
import com.darcy.message.lib_http.config.CONNECT_TIMEOUT
import com.darcy.message.lib_http.config.READ_TIMEOUT
import com.darcy.message.lib_http.config.WRITE_TIMEOUT
import com.darcy.message.lib_http.exts.gsonToBean
import com.darcy.message.lib_http.exts.toUrlEncodedString
import com.darcy.message.lib_http.interceptor.impl.KeyInterceptor
import com.darcy.message.lib_http.request.OkHttpRequestAction
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

object OKHttpClient : IHttpClient {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .callTimeout(CALL_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(KeyInterceptor())
            .build()
    }

    fun okHttpClient(): OkHttpClient {
        return okHttpClient
    }

    override suspend fun <T> doGet(
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        block: OkHttpRequestAction<T>.() -> Unit
    ) {
        val action: OkHttpRequestAction<T> = OkHttpRequestAction<T>().apply(block)
        val url = baseUrl + path + "?" + params.toUrlEncodedString()
        val request: Request =
            Request.Builder().url(url)
                .get()
                .build()
        action.start?.invoke()
        okHttpClient.newCall(request).apply {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    action.error?.invoke("doGet error: ${e.message} url=$url")
                    action.finish?.invoke()
                }

                override fun onResponse(call: Call, response: Response) {
                    action.success?.invoke(response.gsonToBean())
                    action.finish?.invoke()
                }
            })
        }
    }

}