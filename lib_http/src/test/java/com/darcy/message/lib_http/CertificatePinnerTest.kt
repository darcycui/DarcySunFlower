package com.darcy.message.lib_http

import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.InputStream
import java.net.URL
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class CertificatePinnerTest {
    @Test
    fun test() {
        runBlocking {

            val fingerprint = getCertificateFingerprint()
            println("SHA-256 Fingerprint: $fingerprint")
        }
    }

    private fun getCertificateFingerprint(): String? {
        try {
            // 创建 URL 对象
            val url = URL("https://10.0.0.200/UniExServicesWeb/login")
            // 打开 HTTPS 连接
            val connection = url.openConnection() as HttpsURLConnection

            // 配置连接属性
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json; utf-8")
            connection.setRequestProperty("Accept", "application/json")
            connection.connectTimeout = 5000 // 5秒超时
            connection.readTimeout = 5000 // 5秒超时

            // 可选：配置自定义 TrustManager（用于接受自签名证书）
//            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
//                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
//                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
//                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
//            })
//
//            val sslContext = SSLContext.getInstance("TLS")
//            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
//            connection.sslSocketFactory = sslContext.socketFactory

            // 发送请求
            connection.connect()
            // 获取服务器证书链
            val certificates = connection.serverCertificates
            if (certificates.isNotEmpty()) {
                val certificate = certificates[0] as X509Certificate
                // 计算 SHA-256 指纹
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(certificate.encoded)
                // 转换为 Base64 编码
                return android.util.Base64.encodeToString(digest, android.util.Base64.NO_WRAP)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}