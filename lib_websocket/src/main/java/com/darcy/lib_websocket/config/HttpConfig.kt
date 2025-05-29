package com.darcy.lib_websocket.config

import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.print

private const val ONLINE = true

private const val BASE_ONLINE_URL = "https://apis.juhe.cn/"
private const val BASE_TEST_URL = "https://apis.juhe.cn/"
const val CALL_TIMEOUT = 30_000L
const val CONNECT_TIMEOUT = 10_000L
const val READ_TIMEOUT = 10_000L
const val WRITE_TIMEOUT = 10_000L

object HttpConfig {
    fun getBaseHttpUrl(): String {
        return if (ONLINE) BASE_ONLINE_URL else BASE_TEST_URL
    }

    fun getHostName(): String {
        // 从 baseurl 获取host
        return runCatching {
            getBaseHttpUrl().substringBeforeLast("/")
        }.onFailure {
            logE("getHostName error: $it")
            it.print()
        }.getOrElse { "" }
    }
}
