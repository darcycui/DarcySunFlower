package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_common.exts.logW
import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.client.factory.KtorFactory
import com.darcy.message.lib_http.exts.toFormDataContent
import com.darcy.message.lib_http.exts.toUrlEncodedString
import com.darcy.message.lib_http.parser.IJsonParser
import com.darcy.message.lib_http.parser.impl.GsonParserImpl
import com.darcy.message.lib_http.request.CommonRequestAction
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

object KtorHttpClient : IHttpClient {
    private val ktorClient = KtorFactory.create()
    private val jsonParser: IJsonParser by lazy {
        GsonParserImpl()
    }

    override suspend fun <T> doGet(
        clazz: Class<T>,
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
        internalRequest(clazz, responseStr, action)
        // darcyRefactor: 使用自定义 KSerializer 解析 json
//        val bean: BaseResult<T> = ktorClient.get(url).body()
//        if (bean.isSuccess()) {
//            action.success?.invoke(bean)
//        } else {
//            action.error?.invoke("ktor json parse error")
//        }
    }

    override suspend fun <T> doPost(
        clazz: Class<T>,
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
        internalRequest(clazz, jsonString, action)
    }

    private fun <T> internalRequest(
        clazz: Class<T>,
        jsonString: String,
        action: CommonRequestAction<T>
    ) {
        logW("KtorHttpClient http request")
        jsonParser.toBean(jsonString, clazz, action.success, action.successList)
        action.finish?.invoke()
    }
}