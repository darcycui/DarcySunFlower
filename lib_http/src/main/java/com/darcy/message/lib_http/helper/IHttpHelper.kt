package com.darcy.message.lib_http.helper

import com.darcy.message.lib_http.request.CommonRequestAction

interface IHttpHelper {
    suspend fun <T> httpRequest(block: CommonRequestAction<T>.() -> Unit)
}