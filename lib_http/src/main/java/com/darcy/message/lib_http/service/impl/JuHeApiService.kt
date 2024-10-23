package com.darcy.message.lib_http.service.impl

import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.createRetrofitService
import com.darcy.message.lib_http.service.IApiService
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.nio.channels.FileChannel.MapMode

interface JuHeApiService : IApiService {

    companion object {
        private val apiService: JuHeApiService by lazy {
            createRetrofitService(JuHeApiService::class.java)
        }

        fun api() = apiService
    }

    @GET("https://apis.juhe.cn/ip/ipNewV3?key=f128bfc760193c5762c5c3be2a6051d8")
    suspend fun checkIP(@Query("ip") ip: String): BaseResult<IPEntity>

    @POST("https://apis.juhe.cn/ip/ipNewV3")
    @FormUrlEncoded
    suspend fun checkIPPost(@FieldMap params: Map<String, String>?): BaseResult<IPEntity>
}