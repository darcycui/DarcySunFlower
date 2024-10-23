package com.darcy.message.lib_http.exts

import com.darcy.message.lib_http.response.HttpCode
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.getHttpErrorMessage(): HttpCode {
    val ex = this
    return if (ex is ConnectException || ex is UnknownHostException || ex is SocketTimeoutException) {
        HttpCode.NetworkError
    } else {
        HttpCode.UnknownError(message = ex.message ?: "no exception message")
    }
}