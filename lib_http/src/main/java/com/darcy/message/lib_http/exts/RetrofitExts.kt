package com.darcy.message.lib_http.exts

import com.darcy.message.lib_http.config.getBaseHttpUrl
import com.darcy.message.lib_http.helper.impl.RetrofitHttpHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun <T> Any.createRetrofitService(clazz: Class<T>, baseUrl: String = getBaseHttpUrl()): T {
    return Retrofit.Builder()
        .client(RetrofitHttpHelper.okHttpClient())
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(clazz)
}