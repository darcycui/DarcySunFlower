package com.darcy.message.lib_http

import android.util.Log
import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_http.entity.IPEntityAll
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.gsonToBean
import com.darcy.message.lib_http.exts.json
import com.darcy.message.lib_http.exts.jsonToBean
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
    val jsonStringAll2 = """{
  "resultcode":"200",
  "error_code":"0",
  "reason":"success",
  "result":{
    "Country":"中国",
    "Province":"台湾",
    "City":"台北",
    "District":"",
    "Isp":"台湾学术网"
  }
}"""

    @Test
    fun testToJSON() {
        // 测试转换为JSON
        val responseResult: IPEntity = IPEntity("台北", "中国", "District", "台湾学术网", "台湾")
        val responseAll: IPEntityAll = IPEntityAll("200", "查询成功", "SUCCESS", responseResult)
        json.encodeToString(responseResult).also {
            println("responseResult-->json: $it")
        }
        json.encodeToString(responseAll).also {
            println("responseFull-->json: $it")
        }

    }

    @Test
    fun testToBean() {
        // 测试转换为实体
        json.decodeFromString<IPEntity>(jsonStringResult).also {
            println("jsonStringResult-->IPEntity: $it")
        }
        json.decodeFromString<IPEntityAll>(jsonStringAll).also {
            println("jsonStringAll-->IPEntityAll: $it")
        }
        json.decodeFromString<BaseResult<IPEntity>>(jsonStringAll).also {
            println("jsonStringAll-->BaseResult<IPEntity>: $it")
        }

        json.decodeFromString<IPEntityAll>(jsonStringAll2).also {
            println("jsonStringAll2-->IPEntityAll: $it")
        }
        json.decodeFromString<BaseResult<IPEntity>>(jsonStringAll2).also {
            println("jsonStringAll2-->BaseResult<IPEntity>: $it")
        }
        jsonStringAll2.jsonToBean<BaseResult<IPEntity>>().also {
            println("jsonStringAll2-->jsonToBean: $it")
        }

        jsonStringAll2.gsonToBean<BaseResult<IPEntity>>().also {
            println("jsonStringAll-->gsonToBean: $it")
        }
    }

    /**
     * fixme NullPointerException: Parameter specified as non-null is null: method kotlinx.serialization.SerializersKt__SerializersKt.serializer, parameter type
     */
    @Test
    fun testTParamToBean() {
        preFun<IPEntity>()

    }

    private fun <T> preFun() {
        doGet<BaseResult<T>>()
    }

    inline fun <reified T> doGet() {
        val responseStr = jsonStringAll2
        println("responseStr=:$responseStr")
        val result = responseStr.jsonToBean<T>().also {
            println("BaseResult=:$it")
        }
        println("success+++")
    }
}