package com.darcy.message.lib_http

import com.darcy.message.lib_http.client.impl.OKHttpHttpClient
import com.darcy.message.lib_http.entity.IPEntity
import kotlinx.coroutines.runBlocking
import org.junit.Test

class OkHttpClientTest {

    private fun initHttpManager() {
        HttpManager.init(OKHttpHttpClient)
    }

    @Test
    fun doGetTest() {
        runBlocking {
            initHttpManager()
            HttpManager.doGet<IPEntity>(
                IPEntity::class.java,
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
                IPEntity::class.java,
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
    fun doGetTestLocalApi() {
        runBlocking {
            initHttpManager()
            HttpManager.doGet<IPEntity>(
                IPEntity::class.java,
                baseUrl = "http://10.0.0.193:8080",
                path = "/customers",
                params = mapOf()
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