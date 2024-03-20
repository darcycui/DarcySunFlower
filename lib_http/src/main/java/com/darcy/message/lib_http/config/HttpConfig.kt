package com.darcy.message.lib_http.config

private const val ONLINE = true

private const val BASE_ONLINE_URL = "https://apis.juhe.cn/"
private const val BASE_TEST_URL = "https://apis.juhe.cn/"
const val CALL_TIMEOUT = 30_000L
const val CONNECT_TIMEOUT = 10_000L
const val READ_TIMEOUT = 10_000L
const val WRITE_TIMEOUT = 10_000L
fun getBaseHttpUrl(): String {
    return if (ONLINE) BASE_ONLINE_URL else BASE_TEST_URL
}