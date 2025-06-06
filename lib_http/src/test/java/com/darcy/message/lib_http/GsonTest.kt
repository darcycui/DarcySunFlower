package com.darcy.message.lib_http

import com.darcy.message.lib_common.xlog.XLogHelper
import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_http.entity.IPEntityAll
import com.darcy.message.lib_http.entity.UserEntity
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.parser.gsonBeanToJsonString
import com.darcy.message.lib_http.exts.parser.gsonStringToList
import com.darcy.message.lib_http.exts.parser.gsonStringToObject
import com.darcy.message.lib_http.exts.parser.gsonStringToObject2
import com.darcy.message.lib_http.exts.parser.gsonStringToObject3
import org.junit.Before
import org.junit.Test

class GsonTest {
    val jsonStringAll = """{
  "resultcode":200,
  "error_code":0,
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
        responseResult.gsonBeanToJsonString().also {
            println("responseResult-->json: $it")
        }
        responseAll.gsonBeanToJsonString().also {
            println("responseFull-->json: $it")
        }

    }

    @Test
    fun testToObject() {
        // 测试转换为实体
        jsonStringAll.gsonStringToObject<IPEntity>().also {
            println("1.jsonStringAll-->BaseResult<IPEntity>: $it")
//            println("1.jsonStringAll-->BaseResult<IPEntity>: ${it?.result}") // fixme: LinkedTreeMap can not be cast to IPEntity
        }
        jsonStringAll.gsonStringToObject2<IPEntity>(IPEntity::class.java).also {
            println("2.jsonStringAll-->BaseResult<IPEntity>: $it")
            println("2.jsonStringAll-->BaseResult<IPEntity>: ${it?.result}")
        }
        jsonStringAll.gsonStringToObject3<BaseResult<IPEntity>>().also {
            println("3.jsonStringAll-->BaseResult<IPEntity>: $it")
            println("3.jsonStringAll-->IPEntity: ${it?.result}")
            println("3.jsonStringAll-->IPEntity: ${it?.result!!::class.simpleName}")
        }
    }

    val jsonList = """
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
    """.trimIndent()
    @Test
    fun testToList() {
        jsonList.gsonStringToList(UserEntity::class.java).also {
            println("BaseResult=:$it")
            println("BaseResult-->UserEntity:${it?.result}")
            println("BaseResult-->UserEntity: ${it?.result!!::class.simpleName}")
        }
    }

    /**
     * 解决泛型传递丢失：增加 clazz参数 (用于构造 [com.darcy.message.lib_http.type.ParameterizedTypeImpl])
     */
    @Test
    fun testTParamToObject() {
        doGet<IPEntity>()
        doGet2<IPEntity>(IPEntity::class.java)
    }

    fun <T> doGet2(clazz: Class<T>) {
        jsonStringAll.gsonStringToObject2<T>(clazz).also {
            println("BaseResult=:$it")
            println("BaseResult-->IPEntity:${it?.result}")
            println("BaseResult-->IPEntity: ${it?.result!!::class.simpleName}")
        }
        println("success+++")
    }

    inline fun <reified T> doGet() {
        jsonStringAll.gsonStringToObject3<BaseResult<T>>().also {
            println("BaseResult=:$it")
            println("BaseResult-->IPEntity:${it?.result}")
            println("BaseResult-->IPEntity: ${it?.result!!::class.simpleName}")
        }
        println("success+++")
        println("-----------------------------------------------------------------")
    }
}