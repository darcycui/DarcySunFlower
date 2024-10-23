package com.darcy.message.lib_http.exts

import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.response.HttpCode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.json.Json
import okhttp3.Response

val gson = Gson()

val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    coerceInputValues = true
}

fun <T> Response.gsonToBean(): BaseResult<T> {
    val jsonString = this.body?.string() ?: "{}"
    return jsonString.gsonToBean()
}

fun <T> String.gsonToBean(): T {
    // darcyRefactor: gson get real type of BaseResult<T>
    val responseType = object : TypeToken<BaseResult<T>>() {}.type
    return gson.fromJson(this, responseType)
}

inline fun <reified T> String.jsonToBean(): T {
    return json.decodeFromString<T>(this)
}

fun <T> BaseResult<T>?.isSuccess(): Boolean {
    val bool: Boolean = this?.run {
        resultcode == "200"
    } ?: run { false }
    return bool
}

fun <T> BaseResult<T>?.getHttpErrorMessage(): HttpCode {
    if (this == null) return HttpCode.EmptyError
    return when(this.error_code){
        HttpCode.Success.code.toString() -> HttpCode.Success
        HttpCode.ClientError.code.toString() -> HttpCode.ClientError
        HttpCode.ServerError.code.toString() -> HttpCode.ServerError
        else -> HttpCode.UnknownError(this.error_code.toInt(), this.reason)
    }
}