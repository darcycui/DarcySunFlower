package com.darcy.message.lib_http.exts.exception

import com.darcy.message.lib_http.response.HttpError
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.getHttpErrorMessage(): HttpError {
    val ex = this
    return if (ex is ConnectException || ex is UnknownHostException || ex is SocketTimeoutException) {
        HttpError.NetworkError
    } else {
        HttpError.UnknownError(message = ex.message ?: "no exception message")
    }
}