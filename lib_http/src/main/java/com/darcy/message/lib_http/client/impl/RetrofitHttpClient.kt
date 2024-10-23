package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.exts.getHttpErrorMessage
import com.darcy.message.lib_http.exts.isSuccess
import com.darcy.message.lib_http.request.CommonRequestAction
import okhttp3.OkHttpClient

object RetrofitHttpClient : IHttpClient {
    fun okHttpClient(): OkHttpClient {
        return OKHttpHttpClient.okHttpClient()
    }
    override suspend fun <T> doGet(
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        useCache: Boolean,
        block: CommonRequestAction<T>.() -> Unit
    ) {
        internalRequest(block)
    }

    override suspend fun <T> doPost(
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        useCache: Boolean,
        block: CommonRequestAction<T>.() -> Unit
    ) {
        internalRequest(block)
    }

    private suspend fun <T> internalRequest(block: CommonRequestAction<T>.() -> Unit) {
        val action: CommonRequestAction<T> = CommonRequestAction<T>().apply(block)
        try {
            action.start?.invoke()
            val result = action.request?.invoke()
            if (result.isSuccess()) {
                action.success?.invoke(result)
            } else {
                action.error?.invoke(result?.reason ?: "Empty reason")
            }
        } catch (ex: Exception) {
            action.error?.invoke(ex.getHttpErrorMessage().message)
        } finally {
            action.finish?.invoke()
        }
    }
}