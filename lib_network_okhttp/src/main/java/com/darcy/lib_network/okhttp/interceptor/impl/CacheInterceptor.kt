package com.darcy.lib_network.okhttp.interceptor.impl

import com.darcy.lib_network.okhttp.exts.hasNetwork
import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.exts.logV
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * cache interceptor
 */
class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val cacheControl: String = request.headers["Cache-Control"] ?: ""
        if ("no-cache" == cacheControl) {
            logV(message = "force no-cache")
            return chain.proceed(request)
        }
        request = if (AppHelper.getAppContext().hasNetwork()) {
            request.newBuilder()
                .cacheControl(CacheControl.Builder().maxAge(30, TimeUnit.MINUTES).build())
                .build()
        } else {
            request.newBuilder()
                .cacheControl(CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build())
                .build()
        }
        return chain.proceed(request)
    }
}