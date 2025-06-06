package com.darcy.message.lib_http.parser

import com.darcy.message.lib_http.entity.base.BaseResult

interface IJsonParser {
    fun <T> toBean(
        json: String, clazz: Class<T>, success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?
    )
}