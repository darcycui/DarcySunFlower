package com.darcy.message.lib_http

import com.darcy.message.lib_http.client.impl.RetrofitHttpClient
import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_http.service.impl.JuHeApiService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RetrofitHttpClientTest {

    private fun initHttpManager() {
        HttpManager.init(RetrofitHttpClient)
    }

    @Test
    fun doGetTest() {
        runBlocking {
            val api = JuHeApiService.api()
            initHttpManager()
            HttpManager.doGet<IPEntity>(path = "", params = mapOf()) {
                start {
                    println("start")
                }
                request {
                    println("request:")
                    api.checkIP("110.110.110.110")
                }
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
        }
    }

    @Test
    fun doPostTest() {
        runBlocking {
            val api = JuHeApiService.api()
            initHttpManager()
            HttpManager.doPost<IPEntity>(path = "", params = mapOf()) {
                start {
                    println("start")
                }
                request {
                    println("request:")
                    api.checkIPPost(
                        mapOf(
                            "ip" to "110.110.110.110",
                            "key" to "f128bfc760193c5762c5c3be2a6051d8"
                        )
                    )
                }
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
        }
    }
}