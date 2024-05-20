package com.darcy.message.lib_http

import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.config.getBaseHttpUrl
import com.darcy.message.lib_http.helper.IHttpHelper
import com.darcy.message.lib_http.request.CommonRequestAction

/**
 * Created by Darcy
 * HttpManager use Proxy Mode to send http request
 * Strategy:
 * if the [httpHelper] is not null,then use [httpHelper] to do http request
 * else if the [httpHelper] is not null,then use [httpClient] to do http request
 * both [httpHelper] and [httpClient] can not be null at the same time
 */
object HttpManager {
    /**
     * http client used to send http request with the lower priority than [httpHelper]
     * may be null
     */
    private var httpClient: IHttpClient? = null

    /**
     * http helper used to send http request with the higher priority than [httpClient]
     * may be null
     */
    private var httpHelper: IHttpHelper? = null

    /**
     * initialize httpManager HttpClient
     * used for OkHttp/Ktor etc
     */
    fun init(httpClient: IHttpClient) {
        this.httpClient = httpClient
    }

    /**
     * initialize httpManager HttpHelper
     * typically used for Retrofit
     */
    fun init(httpHelper: IHttpHelper) {
        this.httpHelper = httpHelper
    }

    suspend fun <T> doGet(
        baseUrl: String = getBaseHttpUrl(),
        path: String,
        params: Map<String, String>,
        useCache: Boolean = true,
        block: CommonRequestAction<T>.() -> Unit
    ) {
        if (httpHelper != null) {
            httpHelper?.httpRequest(block)
        } else if (httpClient != null) {
            httpClient?.doGet(baseUrl, path, params, useCache, block)
        }
    }
}