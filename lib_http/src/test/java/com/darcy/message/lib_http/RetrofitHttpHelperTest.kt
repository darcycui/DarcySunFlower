package com.darcy.message.lib_http

import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_http.helper.impl.RetrofitHttpHelper
import com.darcy.message.lib_http.service.impl.JuHeApiService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RetrofitHttpHelperTest {

    private fun initHttpManager() {
        HttpManager.init(RetrofitHttpHelper)
    }

    @Test
    fun testHttpRequest() {
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
}