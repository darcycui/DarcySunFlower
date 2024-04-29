package com.darcy.message.lib_http.exts

import com.darcy.message.lib_http.entity.base.BaseResult
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
    val responseType = object : TypeToken<BaseResult<T>>() {}.type
    return gson.fromJson(this, responseType)
}

inline fun <reified T> String.jsonToBean(): T {
    return json.decodeFromString<T>(this)
}