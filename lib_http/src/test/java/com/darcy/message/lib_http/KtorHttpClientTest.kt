package com.darcy.message.lib_http

import com.darcy.message.lib_http.client.impl.KtorHttpClient
import com.darcy.message.lib_http.entity.IPEntity
import kotlinx.coroutines.runBlocking
import org.junit.Test

class KtorHttpClientTest {


    private fun initHttpManager() {
        HttpManager.init(KtorHttpClient)
    }

    @Test
    fun doGetTest() {
        runBlocking {
            initHttpManager()
            HttpManager.doGet<IPEntity>(
                baseUrl = "https://apis.juhe.cn",
                path = "/ip/ipNewV3",
                params = mapOf(
                    "ip" to "120.120.120.120",
                    "key" to "f128bfc760193c5762c5c3be2a6051d8"
                )
            ) {
                start {
                    println("start")
                }
                request { null }
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

    @Test
    fun doPostTest() {
        runBlocking {
            initHttpManager()
            HttpManager.doPost<IPEntity>(
                baseUrl = "https://apis.juhe.cn",
                path = "/ip/ipNewV3",
                params = mapOf(
                    "ip" to "120.120.120.120",
                    "key" to "f128bfc760193c5762c5c3be2a6051d8"
                )
            ) {
                start {
                    println("start")
                }
                request { null }
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
}
