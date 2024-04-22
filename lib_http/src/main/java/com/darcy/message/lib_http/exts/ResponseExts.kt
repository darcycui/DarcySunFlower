package com.darcy.message.lib_http.exts

import com.darcy.message.lib_http.entity.base.BaseResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Response

fun <T> Response.gsonToBean(): BaseResult<T> {
    val responseType = object : TypeToken<BaseResult<T>>() {}.type
    val gson = Gson()
    val jsonString = this.body?.string() ?: "{}"
    return gson.fromJson(jsonString, responseType)
}