package com.darcy.message.lib_http.exts.parser

import com.darcy.message.lib_http.factory.JsonFactory

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

fun Any.jsonBeanToJsonString(): String {
    return kotlinxJson.encodeToString(this)
}