package com.darcy.lib_network.okhttp.interceptor.impl

import com.darcy.lib_network.okhttp.interceptor.IInterceptor
import okhttp3.Interceptor
import okhttp3.Response

/**
 * add header interceptor
 */
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
        request = request.newBuilder()
            // 聚合api key
            .addHeader("key", "f128bfc760193c5762c5c3be2a6051d8")
            // github api token
            .addHeader("Authorization", "token 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")
            .build()
        key = request.headers["key"]
        println("-->key2: $key")
        return chain.proceed(request)
    }
}