package com.darcy.message.lib_http

import com.darcy.message.lib_common.xlog.XLogHelper
import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_http.entity.IPEntityAll
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.parser.jsonStringToObject
import com.darcy.message.lib_http.exts.parser.kotlinxJson
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.junit.Before
import org.junit.Test

class KotlinxSerializationTest {
    val jsonStringResult = """{
  "Country":"中国",
  "Province":"台湾",
  "City":"台北",
  "District":"",
  "Isp":"台湾学术网"
}"""
    val jsonStringAll = """{
  "resultcode":"200",
  "error_code":"0",
  "reason":"SUCCESS",
  "result":{
    "City":"台北",
    "Country":"中国",
    "District":"District",
    "Isp":"台湾学术网",
    "Province":"台湾"
  }
}"""

    @Before
    fun init() {
        XLogHelper.forTest()
    }

    @Test
    fun testToJSON() {
        // 测试转换为JSON
        val responseResult: IPEntity = IPEntity("台北", "中国", "District", "台湾学术网", "台湾")
        val responseAll: IPEntityAll = IPEntityAll(200, 200, "SUCCESS", responseResult)
        kotlinxJson.encodeToString(responseResult).also {
            println("responseResult-->json: $it")
        }
        kotlinxJson.encodeToString(responseAll).also {
            println("responseFull-->json: $it")
        }

    }

    @Test
    fun testToBean() {
        // 测试转换为实体
        jsonStringResult.jsonStringToObject<IPEntity>().also {
            println("jsonStringResult-->IPEntity: $it")
        }
        jsonStringAll.jsonStringToObject<IPEntityAll>().also {
            println("jsonStringAll-->IPEntityAll: $it")
        }
        jsonStringAll.jsonStringToObject<BaseResult<IPEntity>>().also {
            println("jsonStringAll-->BaseResult<IPEntity>: $it")
            println("jsonStringAll-->BaseResult<IPEntity>: ${it.result.city}")
            println("jsonStringAll-->BaseResult<IPEntity>: ${it.result::class.java}")
        }
    }

    /**
     * darcyRefactor: 在非 inline 函数中获取泛型的实际类型, 传递KSerializer参数
     */
    @Test
    fun testDoGet() {
        //
        preFun<IPEntity>(serializer<BaseResult<IPEntity>>())
    }

    fun <T : Any> preFun(resultSerializer: KSerializer<BaseResult<T>>) {
        val jsonParser = JsonParser<T>(jsonStringAll, resultSerializer)
        val result = jsonParser.doGet().also {
            println("parseResult:$it")
            println("parseResult:${it.result}")
            println("parseResult:${it.result::class.java}")
        }
    }

    interface IJsonParser<T> {
        fun doGet(): BaseResult<T>
    }

    class JsonParser<T : Any>(
        val jsonStringAll: String,
        private val resultSerializer: KSerializer<BaseResult<T>> // 显式传递序列化器
    ) : IJsonParser<T> {
        override fun doGet(): BaseResult<T> {
            return internal()
        }

        fun internal(): BaseResult<T> {
            val responseStr = jsonStringAll
            println("responseStr=:$responseStr")
//            val result = responseStr.jsonStringToObject<BaseResult<T>>()
            val result = kotlinxJson.decodeFromString<BaseResult<T>>(resultSerializer, responseStr)
            return result
        }
    }
}