package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.request.OkHttpRequestAction
import com.darcy.message.lib_http.request.RetrofitRequestAction
import okhttp3.OkHttpClient

object RetrofitHttpClient : IHttpClient {

    fun okHttpClient(): OkHttpClient {
        return OKHttpClient.okHttpClient()
    }

    override suspend fun <T> doGet(
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        block: OkHttpRequestAction<T>.() -> Unit
    ) {

    }

}