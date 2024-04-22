package com.darcy.message.lib_http

import com.darcy.message.lib_http.client.impl.OKHttpClient
import com.darcy.message.lib_http.entity.IPEntity
import org.junit.Test

class OkHttpClientTest {

    @Test
    fun doGetTest() {
        OKHttpClient.doGet<IPEntity>(
//            baseUrl = "https://apis.juhe.cn",
            path = "/ip/ipNewV3",
            params = mapOf(
                "ip" to "114.215.154.101",
                "key" to "f128bfc760193c5762c5c3be2a6051d8"
            )
        ) {
            start {
                println("start")
            }
//            request {
//                println("request:")
//            }
            success {
                println("success:$it")
            }
            error {
                println("error:$it")
            }
            finish {
                println("finish")
            }
        }
        Thread.sleep(2_000)
    }
}