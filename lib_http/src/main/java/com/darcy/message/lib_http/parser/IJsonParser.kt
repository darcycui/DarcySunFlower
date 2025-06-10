package com.darcy.message.lib_http.parser

import com.darcy.message.lib_http.entity.base.BaseResult
import kotlinx.serialization.KSerializer

interface IJsonParser {
    fun <T> toBean(
        json: String,
        clazz: Class<T>?,
        kSerializer: KSerializer<T>?,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        error: ((String) -> Unit)?
    )
}