package com.darcy.message.lib_http

import com.darcy.message.lib_common.xlog.XLogHelper
import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.parser.jsonStringToObject
import com.darcy.message.lib_http.exts.parser.kotlinxJson
import org.junit.Before
import org.junit.Test

class KSerializerTest {
    val jsonStringResult = """{ 
    "City":"台北",
    "Country":"中国",
    "District":"District",
    "Isp":"台湾学术网",
    "Province":"台湾",
    "time":"2025-06-09 10:08:59"
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

    /**
     * fixme 如何在非 inline 函数中获取泛型的实际类型
     */
    @Test
    fun testLocalDateTimeSerializer() {
        val ipEntity = IPEntity(
            city = "台北",
            country = "中国",
            district = "District",
            isp = "台湾学术网",
            province = "台湾"
        )
        kotlinxJson.encodeToString(ipEntity).also {
            println("ipEntity-->$it")
        }
        // 测试转换为实体
//        jsonStringResult.jsonStringToObject<IPEntity>().also {
//            println("jsonString-->IPEntity: $it")
//        }
        jsonStringAll.jsonStringToObject<BaseResult<IPEntity>>().also {
            println("jsonStringAll-->BaseResult<IPEntity>: $it")
            println("jsonStringAll-->BaseResult<IPEntity>: ${it.result}")
            println("jsonStringAll-->BaseResult<IPEntity>: ${it.result::class.java}")
        }
    }

    @Test
    fun testDoGet() {
        preFun<IPEntity>()
    }

    inline fun <reified T> preFun() {
        internal<BaseResult<T>>()
    }

    inline fun <reified T : BaseResult<*>> internal() {
        val responseStr = jsonStringAll
        println("responseStr=:$responseStr")
        val result = responseStr.jsonStringToObject<T>().also {
            println("BaseResult=:$it")
            println("BaseResult.result=:${it.result}")
            println("BaseResult=:${it::class.java}")
        }
        println("success+++")
    }
}