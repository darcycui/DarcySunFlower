package com.darcy.message.lib_http

import com.darcy.message.lib_http.helper.impl.CommonHttpHelper
import com.darcy.message.lib_http.service.impl.JuHeApiService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CommonHttpHelperTest {
    @Test
    fun testHttpRequest() {
        val api = JuHeApiService.api()
        runBlocking{
            CommonHttpHelper.httpRequest {
                start {
                    println("start")
                }
                request {
                    api.checkIP("110.110.110.110")
                }
                success {
                    println("success")
                }
                error {
                    println("error")
                }
                finish {
                    println("finish")
                }
            }
        }
    }
}