package com.darcy.message.lib_http

import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.service.impl.JuHeApiService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class JuHeApiServiceTest {
    @Test
    fun testCheckIP() {
        val api = JuHeApiService.api()
        runBlocking{
            val repo: BaseResult<IPEntity> = api.checkIP("110.110.110.110")
            println("repo=$repo")
        }
    }
}