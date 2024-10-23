package com.darcy.message.lib_http.client.factory

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.gson.gson
import io.ktor.serialization.kotlinx.json.json

object KtorFactory {
    /**
     * use DSL init KtorClient
     */
    fun create(): HttpClient {
        return HttpClient(OkHttp) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.ALL
            }
            // fixme 自动序列化json为对象的配置
//            install(ContentNegotiation) {
//                json(com.darcy.message.lib_http.exts.json)
//                gson(
//                    contentType = ContentType.Any // workaround for broken APIs
//                )
//            }
        }
    }
}