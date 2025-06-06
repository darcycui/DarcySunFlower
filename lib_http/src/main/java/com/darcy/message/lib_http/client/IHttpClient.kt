package com.darcy.message.lib_http.client

import com.darcy.message.lib_http.config.HttpConfig
import com.darcy.message.lib_http.request.CommonRequestAction

interface IHttpClient {
    suspend fun <T> doGet(
        clazz: Class<T>,
        baseUrl: String = HttpConfig.getBaseHttpUrl(),
        path: String,
        params: Map<String, String>,
        useCache: Boolean = true,
        block: CommonRequestAction<T>.() -> Unit,
    )

    suspend fun <T> doPost(
        clazz: Class<T>,
        baseUrl: String = HttpConfig.getBaseHttpUrl(),
        path: String,
        params: Map<String, String>,
        useCache: Boolean = true,
        block: CommonRequestAction<T>.() -> Unit,
    )
//
//  suspend  fun doPut()
//
//  suspend  fun doDelete()
//
//  suspend fun doUpload() // val body:RequestBody = file.toRequestBody
//
//  suspend fun doDownload()
}