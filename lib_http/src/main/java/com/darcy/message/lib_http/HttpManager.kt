package com.darcy.message.lib_http

import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.request.CommonRequestAction

/**
 * Created by Darcy
 * HttpManager use Proxy Mode to send http request
 */
object HttpManager : IHttpClient {
    /**
     * http client used to send http request
     * may be null
     */
    private var httpClient: IHttpClient? = null

    /**
     * initialize httpManager HttpClient
     * used for OkHttp/Ktor/Retrofit etc.
     */
    fun init(httpClient: IHttpClient) {
        this.httpClient = httpClient
    }

    override suspend fun <T> doGet(
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        useCache: Boolean,
        block: CommonRequestAction<T>.() -> Unit
    ) {
        checkHttpClientInit()
        httpClient?.doGet(baseUrl, path, params, useCache, block)
    }

    override suspend fun <T> doPost(
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        useCache: Boolean,
        block: CommonRequestAction<T>.() -> Unit
    ) {
        checkHttpClientInit()
        httpClient?.doPost(baseUrl, path, params, useCache, block)
    }

    private fun checkHttpClientInit() {
        if (httpClient == null) {
            throw IllegalStateException("HttpManager not init")
        }
    }
}