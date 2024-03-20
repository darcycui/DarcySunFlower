package com.darcy.message.lib_http.service.impl

import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.createRetrofitService
import com.darcy.message.lib_http.service.IApiService
import retrofit2.http.GET
import retrofit2.http.Query

interface JuHeApiService : IApiService {
    @GET("https://apis.juhe.cn/ip/ipNewV3?key=f128bfc760193c5762c5c3be2a6051d8")
    suspend fun checkIP(@Query("ip") ip: String): BaseResult<IPEntity>

    companion object {
        private val apiService: JuHeApiService by lazy {
            createRetrofitService(JuHeApiService::class.java)
        }

        fun api() = apiService
    }
}