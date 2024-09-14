package com.darcy.message.lib_http.client.impl

import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.config.CALL_TIMEOUT
import com.darcy.message.lib_http.config.CONNECT_TIMEOUT
import com.darcy.message.lib_http.config.READ_TIMEOUT
import com.darcy.message.lib_http.config.WRITE_TIMEOUT
import com.darcy.message.lib_http.exts.gsonToBean
import com.darcy.message.lib_http.exts.toUrlEncodedString
import com.darcy.message.lib_http.interceptor.impl.KeyInterceptor
import com.darcy.message.lib_http.request.CommonRequestAction
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI
import java.util.concurrent.TimeUnit

object OKHttpHttpClient : IHttpClient {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
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
            // retry when network is not available
            .retryOnConnectionFailure(true)
            // do not use system proxy
            .proxy(Proxy.NO_PROXY)
            .proxySelector(object : ProxySelector() {
                override fun select(uri: URI?): MutableList<Proxy> {
                    return mutableListOf(Proxy.NO_PROXY)
                }

                override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {
                    println("ProxySelector connectFailed: uri=$uri sa=$sa ioe=$ioe")
                }

            })
            .build()
    }

    fun okHttpClient(): OkHttpClient {
        return okHttpClient
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
        val request: Request =
            Request.Builder().url(url)
                .header("Cache-Control", if (useCache) "" else "no-cache")
                .get()
                .build()
        action.start?.invoke()
        okHttpClient.newCall(request).apply {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    action.error?.invoke("doGet error: ${e.message} url=$url")
                    action.finish?.invoke()
                }

                override fun onResponse(call: Call, response: Response) {
                    action.success?.invoke(response.gsonToBean<T>())
                    action.finish?.invoke()
                }
            })
        }
    }

}