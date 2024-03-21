package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.config.CALL_TIMEOUT
import com.darcy.message.lib_http.config.CONNECT_TIMEOUT
import com.darcy.message.lib_http.config.READ_TIMEOUT
import com.darcy.message.lib_http.config.WRITE_TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OKHttpClient : IHttpClient {

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .callTimeout(CALL_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

}