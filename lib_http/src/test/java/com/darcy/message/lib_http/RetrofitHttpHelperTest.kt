package com.darcy.message.lib_http

import com.darcy.message.lib_http.helper.impl.RetrofitHttpHelper
import com.darcy.message.lib_http.service.impl.JuHeApiService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RetrofitHttpHelperTest {
    @Test
    fun testHttpRequest() {
        val api = JuHeApiService.api()
        runBlocking{
            RetrofitHttpHelper.httpRequest {
                start {
                    println("start")
                }
                request {
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