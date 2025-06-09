package com.darcy.message.lib_http.parser.impl

import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.parser.gsonStringToList
import com.darcy.message.lib_http.exts.parser.gsonStringToObject2
import com.darcy.message.lib_http.parser.IJsonParser
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.serialization.KSerializer

class GsonParserImpl : IJsonParser {
    override fun <T> toBean(
        json: String,
        clazz: Class<T>?,
        kSerializer: KSerializer<T>?,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?
    ) {
        val jsonObject: JsonObject = JsonParser.parseString(json).asJsonObject
        // 根据result字段判断是object还是array
        val resultElement: JsonElement = jsonObject.get("result")
        when {
            resultElement.isJsonObject -> {
                clazz?.let {
                    success?.invoke(json.gsonStringToObject2(clazz))
                } ?: run {
                    logE("resultElement is JsonObject, but clazz is null")
                }
            }

            resultElement.isJsonArray -> {
                clazz?.let {
                    successList?.invoke(json.gsonStringToList(clazz))
                } ?: run {
                    logE("resultElement is JsonArray, but clazz is null")
                }
            }

            else -> {
                throw Exception("json result is not object or array")
            }
        }
    }
}