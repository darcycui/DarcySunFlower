package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_common.exts.logW
import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.exts.exception.getHttpErrorMessage
import com.darcy.message.lib_http.exts.isSuccess
import com.darcy.message.lib_http.request.CommonRequestAction
import kotlinx.serialization.KSerializer
import okhttp3.OkHttpClient

object RetrofitHttpClient : IHttpClient {
    fun okHttpClient(): OkHttpClient {
        return OKHttpHttpClient.okHttpClient()
    }

    override suspend fun <T> doGet(
        clazz: Class<T>,
        kSerializer: KSerializer<T>,
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        useCache: Boolean,
        block: CommonRequestAction<T>.() -> Unit
    ) {
        internalRequest(block)
    }

    override suspend fun <T> doPost(
        clazz: Class<T>,
        kSerializer: KSerializer<T>,
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        useCache: Boolean,
        block: CommonRequestAction<T>.() -> Unit
    ) {
        internalRequest(block)
    }

    private suspend fun <T> internalRequest(block: CommonRequestAction<T>.() -> Unit) {
        logW("RetrofitHttpClient http request")
        val action: CommonRequestAction<T> = CommonRequestAction<T>().apply(block)
        try {
            action.start?.invoke()
            if (action.requestList != null) {
                dealList(action)
            } else if (action.request != null) {
                dealObject(action)
            } else {
                action.error?.invoke("Either request or requestList is needed.")
            }
        } catch (ex: Exception) {
            action.error?.invoke(ex.getHttpErrorMessage().message)
        } finally {
            action.finish?.invoke()
        }
    }

    private suspend fun <T> dealList(action: CommonRequestAction<T>) {
        val result = action.requestList?.invoke()
        if (result.isSuccess()) {
            action.successList?.invoke(result)
        } else {
            action.error?.invoke(result?.reason ?: "requestList error")
        }
    }

    private suspend fun <T> dealObject(action: CommonRequestAction<T>) {
        val result = action.request?.invoke()
        if (result.isSuccess()) {
            action.success?.invoke(result)
        } else {
            action.error?.invoke(result?.reason ?: "request error")
        }
    }
}