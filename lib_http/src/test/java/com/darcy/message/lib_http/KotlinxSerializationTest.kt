package com.darcy.message.lib_http

import com.darcy.message.lib_common.xlog.XLogHelper
import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_http.entity.IPEntityAll
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.parser.jsonBeanToJsonString
import com.darcy.message.lib_http.exts.parser.jsonStringToObject
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
        responseResult.jsonBeanToJsonString().also {
            println("responseResult-->json: $it")
        }
        responseAll.jsonBeanToJsonString().also {
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
        }
    }

    /**
     * fixme 如何在非 inline 函数中获取泛型的实际类型
     */
    @Test
    fun testTParamToBean() {
//        doGet<BaseResult<IPEntity>>()
        preFun<IPEntity>()
    }

    inline fun <reified T> preFun() {
        doGet<BaseResult<T>>()
    }

    inline fun <reified T> doGet() {
        val responseStr = jsonStringAll
        println("responseStr=:$responseStr")
        val result = responseStr.jsonStringToObject<T>().also {
            println("BaseResult=:$it")
        }
        println("success+++")
    }
}