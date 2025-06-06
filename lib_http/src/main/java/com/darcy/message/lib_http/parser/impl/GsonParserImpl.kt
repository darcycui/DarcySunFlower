package com.darcy.message.lib_http.parser.impl

import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.parser.gsonStringToList
import com.darcy.message.lib_http.exts.parser.gsonStringToObject2
import com.darcy.message.lib_http.parser.IJsonParser
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class GsonParserImpl : IJsonParser {
    override fun <T> toBean(
        json: String, clazz: Class<T>, success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?
    ) {
        val jsonObject: JsonObject = JsonParser.parseString(json).asJsonObject
        // 根据result字段判断是object还是array
        val resultElement: JsonElement = jsonObject.get("result")
        when {
            resultElement.isJsonObject -> {
                success?.invoke(json.gsonStringToObject2(clazz))
            }

            resultElement.isJsonArray -> {
                successList?.invoke(json.gsonStringToList(clazz))
            }

            else -> {
                throw Exception("json result is not object or array")
            }
        }
    }
}