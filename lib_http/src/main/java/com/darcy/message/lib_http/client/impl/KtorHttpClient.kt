package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.client.factory.KtorFactory
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.gsonToBean
import com.darcy.message.lib_http.exts.toFormDataContent
import com.darcy.message.lib_http.exts.toUrlEncodedString
import com.darcy.message.lib_http.request.CommonRequestAction
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

object KtorHttpClient : IHttpClient {
    private val ktorClient = KtorFactory.create()

    override suspend fun <T> doGet(
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        useCache: Boolean,
        block: CommonRequestAction<T>.() -> Unit
    ) {
        val action: CommonRequestAction<T> = CommonRequestAction<T>().apply(block)
        val url = baseUrl + path + "?" + params.toUrlEncodedString()
        action.start?.invoke()
        val responseStr: String = ktorClient.get(url) {
            this.header("User-Agent", "Android Client by Ktor")
        }.body()
        internalRequest(responseStr, action)
    }

    private fun <T> internalRequest(
        responseStr: String,
        action: CommonRequestAction<T>
    ) {
        println("responseStr=:$responseStr")
        val result = responseStr.gsonToBean<BaseResult<T>>().also {
            println("BaseResult=:$it")
        }
        action.success?.invoke(result)
        action.finish?.invoke()
    }

    override suspend fun <T> doPost(
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        useCache: Boolean,
        block: CommonRequestAction<T>.() -> Unit
    ) {
        val action: CommonRequestAction<T> = CommonRequestAction<T>().apply(block)
        val url = baseUrl + path
        val formDataContent = params.toFormDataContent()
        action.start?.invoke()
        val responseStr: String = ktorClient.post(url) {
            this.header("User-Agent", "Android Client by Ktor")
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(formDataContent)
        }.body()
        internalRequest(responseStr, action)
    }
}