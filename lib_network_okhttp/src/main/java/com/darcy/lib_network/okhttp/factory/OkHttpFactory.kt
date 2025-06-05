package com.darcy.lib_network.okhttp.factory

import com.darcy.lib_network.okhttp.config.CALL_TIMEOUT
import com.darcy.lib_network.okhttp.config.CONNECT_TIMEOUT
import com.darcy.lib_network.okhttp.config.READ_TIMEOUT
import com.darcy.lib_network.okhttp.config.WRITE_TIMEOUT
import com.darcy.lib_network.okhttp.interceptor.impl.KeyInterceptor
import com.darcy.lib_network.okhttp.safety.TrustCertHelper
import com.darcy.lib_network.okhttp.safety.VerifyHostHelper
import com.darcy.message.lib_common.exts.logE
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

object OkHttpFactory {
    fun create(): OkHttpClient {
        return OkHttpClient.Builder()
            // set timeout
            .callTimeout(CALL_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            // allow retry
            .retryOnConnectionFailure(true)
            // log
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            // add header
            .addInterceptor(KeyInterceptor())
            // use cache
//            .cache(Cache(AppHelper.getAppContext().filesDir, 1024 * 1024 * 10))
//            .addInterceptor(CacheInterceptor())
            // increase the number of requests for per host, so that the timeout could be correctly(without waiting time in queue)
            .dispatcher(Dispatcher().apply { maxRequestsPerHost = 10 })
            // do not use system proxy
            .proxy(Proxy.NO_PROXY)
//            .proxySelector(object : ProxySelector() {
//                override fun select(uri: URI?): MutableList<Proxy> {
//                    return mutableListOf(Proxy.NO_PROXY)
//                }
//
//                override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {
//                    println("ProxySelector connectFailed: uri=$uri sa=$sa ioe=$ioe")
//                }
//            })
            .apply {
                 // 1.trust build in cert only
//                val sslSocketPair = TrustCertHelper.createTrustAllSSLSocketFactory()
                val trustManager = TrustCertHelper.createTrustBuildIn()
                trustManager?.let {
                    val sslContext = SSLContext.getInstance("TLS")
                    sslContext.init(null, arrayOf(trustManager), java.security.SecureRandom())
                    sslSocketFactory(sslContext.socketFactory, it)
                } ?: run {
                    logE("ERROR: trustManager is null")
                }
            }
            // 2.verify host
            .hostnameVerifier(VerifyHostHelper())
            // 3.add CertificatePinner(can't used for self-signed cert)
//            .certificatePinner(CertPinnerHelper.createCertPinner())
            .build()
    }
}