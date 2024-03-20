package com.darcy.message.lib_http.entity.base

data class BaseResult<T>(
    val resultcode: String = "",
    val error_code: String = "",
    val reason: String = "",
    val data: T
)
