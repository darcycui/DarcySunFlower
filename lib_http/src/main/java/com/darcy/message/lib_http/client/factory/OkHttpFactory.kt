package com.darcy.message.lib_http.client.factory

import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_http.client.impl.okhttp.TrustCertHelper
import com.darcy.message.lib_http.client.impl.okhttp.VerifyHostHelper
import com.darcy.message.lib_http.config.CALL_TIMEOUT
import com.darcy.message.lib_http.config.CONNECT_TIMEOUT
import com.darcy.message.lib_http.config.READ_TIMEOUT
import com.darcy.message.lib_http.config.WRITE_TIMEOUT
import com.darcy.message.lib_http.interceptor.impl.KeyInterceptor
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

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
//            .proxy(Proxy.NO_PROXY)
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
                val sslSocketPair = TrustCertHelper.createTrustBuildInSSLSocketFactoryPair(AppHelper.getAppContext())
                sslSocketPair.let { pair ->
                    if (pair.first != null && pair.second != null) {
                        sslSocketFactory(pair.first!!, pair.second!!)
                        println(message = "sslSocketFactory enabled")
                        logI(message = "sslSocketFactory enabled")
                    } else {
                        println(message = "sslSocketFactory is null")
                        logE(message = "sslSocketFactory is null")
                        throw IllegalStateException("sslSocketFactory is null")
                    }
                }
            }
            // 2.verify host
            .hostnameVerifier(VerifyHostHelper())
            // 3.add CertificatePinner(can't used for self-signed cert)
//            .certificatePinner(CertPinnerHelper.createCertPinner())
            .build()
    }
}