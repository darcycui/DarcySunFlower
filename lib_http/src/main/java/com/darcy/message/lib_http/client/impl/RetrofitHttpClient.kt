package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.config.CALL_TIMEOUT
import com.darcy.message.lib_http.config.CONNECT_TIMEOUT
import com.darcy.message.lib_http.config.READ_TIMEOUT
import com.darcy.message.lib_http.config.WRITE_TIMEOUT
import com.darcy.message.lib_http.interceptor.impl.KeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class RetrofitHttpClient : IHttpClient {

    companion object {

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
    }

}