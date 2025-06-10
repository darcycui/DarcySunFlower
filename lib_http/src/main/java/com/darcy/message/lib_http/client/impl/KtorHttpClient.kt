package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_common.exts.logW
import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.client.factory.KtorFactory
import com.darcy.message.lib_http.exts.toFormDataContent
import com.darcy.message.lib_http.exts.toUrlEncodedString
import com.darcy.message.lib_http.parser.IJsonParser
import com.darcy.message.lib_http.parser.impl.JsonParserImpl
import com.darcy.message.lib_http.request.CommonRequestAction
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.KSerializer

object KtorHttpClient : IHttpClient {
    private val ktorClient = KtorFactory.create()
    private val jsonParser: IJsonParser by lazy {
        JsonParserImpl()
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
        val action: CommonRequestAction<T> = CommonRequestAction<T>().apply(block)
        val url = baseUrl + path + "?" + params.toUrlEncodedString()
        action.start?.invoke()
        val responseStr: String = ktorClient.get(url) {
            this.header("User-Agent", "Android Client by Ktor")
        }.body()
        // darcyRefactor: 使用自定义 KSerializer 解析 json
        internalRequest(kSerializer, responseStr, action)
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
        val action: CommonRequestAction<T> = CommonRequestAction<T>().apply(block)
        val url = baseUrl + path
        val formDataContent = params.toFormDataContent()
        action.start?.invoke()
        val jsonString: String = ktorClient.post(url) {
            this.header("User-Agent", "Android Client by Ktor")
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(formDataContent)
        }.body()
        internalRequest(kSerializer, jsonString, action)
    }

    private fun <T> internalRequest(
        kSerializer: KSerializer<T>,
        jsonString: String,
        action: CommonRequestAction<T>
    ) {
        logW("KtorHttpClient http request")
        jsonParser.toBean(
            jsonString,
            null,
            kSerializer,
            action.success,
            action.successList,
            action.error
        )
        action.finish?.invoke()
    }
}