package com.darcy.message.lib_http.exts

import com.darcy.message.lib_http.client.impl.RetrofitHttpClient
import com.darcy.message.lib_http.config.getBaseHttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun <T> Any.createRetrofitService(clazz: Class<T>, baseUrl: String = getBaseHttpUrl()): T {
    return Retrofit.Builder()
        .client(RetrofitHttpClient.okHttpClient())
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(clazz)
}