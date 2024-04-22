package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.entity.IPEntityAll
import com.darcy.message.lib_http.exts.toUrlEncodedString
import com.darcy.message.lib_http.request.OkHttpRequestAction
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient : IHttpClient {
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
        install(ContentNegotiation) {
            json(Json{
                prettyPrint = true
                isLenient = true
            })
        }
    }

    override suspend fun <T> doGet(
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        block: OkHttpRequestAction<T>.() -> Unit
    ) {
        val action: OkHttpRequestAction<T> = OkHttpRequestAction<T>().apply(block)
        val url = baseUrl + path + "?" + params.toUrlEncodedString()
        action.start?.invoke()
        val result: IPEntityAll = ktorClient.get(url) {
            this.header("User-Agent", "Android Client by Ktor")
        }.body()
//        action.success?.invoke(result)
        println("success:$result")
        action.finish?.invoke()
    }
}