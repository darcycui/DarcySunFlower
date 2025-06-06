package com.darcy.message.lib_http.client.factory

import com.darcy.lib_network.okhttp.safety.TrustCertHelper
import com.darcy.lib_network.okhttp.safety.VerifyHostHelper
import com.darcy.message.lib_http.exts.parser.kotlinxJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import javax.net.ssl.SSLContext

object KtorFactory {

    /**
     * use DSL init KtorClient
     */
    fun create(): HttpClient {
        val trustManager = TrustCertHelper.createTrustBuildIn()
        if (trustManager == null) {
            throw IllegalStateException("trustManager is null")
        }
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), java.security.SecureRandom())

        return HttpClient(OkHttp) {
            engine {
                // 设置Engine SSL
                // https://ktor.io/docs/client-ssl.html
                config {
                    sslSocketFactory(
                        sslContext.socketFactory,
                        trustManager
                    )
                    hostnameVerifier(VerifyHostHelper())
                }
            }
            // 添加log插件
            // https://ktor.io/docs/client-plugins.html
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.ALL
            }
            // fixme 使用 Serialization 解析 json为泛型实体类
            // https://ktor.io/docs/client-serialization.html
            // https://ktor.io/docs/client-responses.html
            install(ContentNegotiation) {
                json(kotlinxJson)
//                gson(
//                    contentType = ContentType.Any // workaround for broken APIs
//                )
            }
        }
    }
}