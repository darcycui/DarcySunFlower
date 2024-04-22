package com.darcy.message.lib_http.client

import com.darcy.message.lib_http.config.getBaseHttpUrl
import com.darcy.message.lib_http.request.OkHttpRequestAction

interface IHttpClient {
    fun <T> doGet(
        baseUrl: String = getBaseHttpUrl(), path: String, params: Map<String, String>,
        block: OkHttpRequestAction<T>.() -> Unit
    )

//    fun doPost()
//
//    fun doPut()
//
//    fun doDelete()
//
//    fun doUpload()
//
//    fun doDownload()
}