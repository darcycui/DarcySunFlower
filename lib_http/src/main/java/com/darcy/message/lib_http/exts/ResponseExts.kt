package com.darcy.message.lib_http.exts

import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.response.HttpError


fun <T> BaseResult<T>?.isSuccess(): Boolean {
    val bool: Boolean = this?.run {
        resultCode == 200
    } ?: run { false }
    return bool
}

fun <T> BaseResult<T>?.getErrorMessage(): HttpError {
    if (this == null) return HttpError.EmptyError
    return when (this.errorCode) {
        HttpError.Success.code -> HttpError.Success
        HttpError.ClientError.code -> HttpError.ClientError
        HttpError.ServerError.code -> HttpError.ServerError
        else -> HttpError.UnknownError(this.errorCode.toInt(), this.reason)
    }
}