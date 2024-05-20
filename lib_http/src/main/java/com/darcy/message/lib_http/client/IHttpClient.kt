package com.darcy.message.lib_http.client

import com.darcy.message.lib_http.config.getBaseHttpUrl
import com.darcy.message.lib_http.request.CommonRequestAction

interface IHttpClient {
    suspend fun <T> doGet(
        baseUrl: String = getBaseHttpUrl(),
        path: String,
        params: Map<String, String>,
        useCache: Boolean = true,
        block: CommonRequestAction<T>.() -> Unit,
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