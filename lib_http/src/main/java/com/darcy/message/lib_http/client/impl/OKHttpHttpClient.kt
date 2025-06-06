package com.darcy.message.lib_http.client.impl

import com.darcy.lib_network.okhttp.factory.OkHttpFactory
import com.darcy.message.lib_common.exts.logW
import com.darcy.message.lib_http.client.IHttpClient
import com.darcy.message.lib_http.exts.toFormBody
import com.darcy.message.lib_http.exts.toUrlEncodedString
import com.darcy.message.lib_http.parser.IJsonParser
import com.darcy.message.lib_http.parser.impl.GsonParserImpl
import com.darcy.message.lib_http.request.CommonRequestAction
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException


object OKHttpHttpClient : IHttpClient {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpFactory.create()
    }
    private val jsonParser: IJsonParser by lazy {
        GsonParserImpl()
    }

    fun okHttpClient(): OkHttpClient {
        return okHttpClient
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
        val request: Request =
            Request.Builder().url(url)
//                .header("Cache-Control", if (useCache) "" else "no-cache")
                .get()
                .build()
        internalRequest<T>(clazz, request, action)
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
        // form params
        val formBody: RequestBody = params.toFormBody()

        val request: Request =
            Request.Builder().url(url)
//                .header("Cache-Control", if (useCache) "" else "no-cache")
                .header("Content-Type", "application/json;charset=utf-8")
                .post(formBody)
                .build()
        internalRequest<T>(clazz, request, action)
    }

    private fun <T> internalRequest(
        clazz: Class<T>,
        request: Request,
        action: CommonRequestAction<T>,
    ) {
        logW("OkHttpClient http request")
        action.start?.invoke()
        okHttpClient.newCall(request).apply {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    action.error?.invoke("doGet error: ${e.message} url=${call.request().url}")
                    action.finish?.invoke()
                }

                override fun onResponse(call: Call, response: Response) {
                    val jsonString = response.body?.string() ?: "{}"
                    jsonParser.toBean(jsonString, clazz, action.success, action.successList)
//                    action.success?.invoke(jsonString.jsonStringToObject<BaseResult<T>>())
                    action.finish?.invoke()
                }
            })
        }
    }

}