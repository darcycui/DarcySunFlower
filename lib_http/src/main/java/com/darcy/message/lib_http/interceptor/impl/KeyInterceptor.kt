package com.darcy.message.lib_http.interceptor.impl

import com.darcy.message.lib_http.interceptor.IInterceptor
import okhttp3.Interceptor
import okhttp3.Response

class KeyInterceptor : IInterceptor, Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url
        val headers = request.headers
        val method = request.method
        val body = request.body
        println("-->URL: $url")
        println("-->Header: $headers")
        println("-->method: $method")

        var key = headers["key"]
        println("-->key1: $key")
        if (key.isNullOrEmpty()) {
            request = request.newBuilder()
                .addHeader("key", "f128bfc760193c5762c5c3be2a6051d8")
                .build()
        }
        key = request.headers["key"]
        println("-->key2: $key")
        return chain.proceed(request)
    }
}