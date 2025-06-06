package com.darcy.message.lib_http.exts.parser

import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.factory.GsonFactory
import com.darcy.message.lib_http.type.ParameterizedTypeImpl
import com.google.gson.reflect.TypeToken

val gson = GsonFactory.createGson()

/*
  {
      "resultcode": 200,
      "error_code": 0,
      "reason": "success",
      "result": {
          "Country": "中国",
          "Province": "",
          "City": "",
          "District": "",
          "Isp": "Zenlayer Inc"
      }
  }
 */
// darcyRefactor: 泛型擦除 T被解析为 LinkedTreeMap
fun <T> String.gsonStringToObject(): BaseResult<T>? {
    val responseType = object : TypeToken<BaseResult<T>>() {}.type
    logE("responseType==$responseType")
    return gson.fromJson(this, responseType)
}

// 解决泛型传递丢失：增加 clazz参数 (用于构造 [com.darcy.message.lib_http.type.ParameterizedTypeImpl])
fun <T> String.gsonStringToObject2(clazz: Class<T>): BaseResult<T>? {
    val responseType = ParameterizedTypeImpl(BaseResult::class.java, arrayOf(clazz))
    logE("responseType==$responseType")
    return gson.fromJson(this, responseType)
}

// 使用 inline + reified 实现泛型解析
inline fun <reified T> String.gsonStringToObject3(): T? {
    val responseType = object : TypeToken<T>() {}.type
    logE("responseType==$responseType")
    return gson.fromJson(this, responseType)
}

fun Any.gsonBeanToJsonString(): String {
    return gson.toJson(this)
}

/*
  {
  	"resultcode": 200,
  	"error_code": -1,
  	"reason": "",
  	"result": [
        {
            "id": 1,
            "name": "Tom"
        },
        {
            "id": 2,
            "name": "Jerry"
        },
        {
            "id": 3,
            "name": "White Princess"
        },
        {
            "id": 4,
            "name": "Winne Bear"
        },
        {
            "id": 5,
            "name": "Cinderella"
        }
  	]
  }
 */
fun <T> String.gsonStringToList(clazz: Class<T>): BaseResult<List<T>>? {
    val listType = ParameterizedTypeImpl(List::class.java, arrayOf(clazz))
    val responseType = ParameterizedTypeImpl(BaseResult::class.java, arrayOf(listType))
    return gson.fromJson(this, responseType)
}