package com.darcy.message.lib_http.exts.parser

import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.factory.JsonFactory
import kotlinx.serialization.KSerializer

val kotlinxJson = JsonFactory.createJson()

/*
  {
      "resultcode": "200",
      "reason": "success",
      "result": {
          "Country": "中国",
          "Province": "",
          "City": "",
          "District": "",
          "Isp": "Zenlayer Inc"
      },
      "error_code": 0
  }
 */
inline fun <reified T> String.jsonStringToObject(): T {
    return kotlinxJson.decodeFromString<T>(this)
}

inline fun <reified T> String.jsonStringToObject2(kSerializer: KSerializer<BaseResult<T>>): BaseResult<T> {
    return kotlinxJson.decodeFromString<BaseResult<T>>(kSerializer, this)
}

//fun Any.jsonBeanToJsonString(): String {
//    return kotlinxJson.encodeToString(this)
//}