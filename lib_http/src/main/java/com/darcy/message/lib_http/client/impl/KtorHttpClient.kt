package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.gsonToBean
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

object KtorHttpClient : IHttpClient {
    // use DSL init ktorClient
    private val ktorClient = HttpClient(OkHttp) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.ALL
        }
//        install(ContentNegotiation) {
//            json(com.darcy.message.lib_http.exts.json)
//            gson(
//                contentType = ContentType.Any // workaround for broken APIs
//            )
//        }
    }

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
        println("responseStr=:$responseStr")
        val result = responseStr.gsonToBean<BaseResult<T>>().also {
            println("BaseResult=:$it")
        }
        action.success?.invoke(result)
        action.finish?.invoke()
    }
}