package com.darcy.message.lib_http.service.impl

import com.darcy.message.lib_http.entity.UserEntity
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.exts.createRetrofitService
import com.darcy.message.lib_http.service.IApiService
import retrofit2.http.GET

interface DarcyApiService : IApiService {

    companion object {
        private val apiService: DarcyApiService by lazy {
            createRetrofitService(DarcyApiService::class.java)
        }

        fun api() = apiService
    }

    @GET("https://darcycui.com.cn/users/all")
    suspend fun getUsers(): BaseResult<List<UserEntity>>

    @GET("https://10.0.0.241/users/all")
    suspend fun getUsersIP(): BaseResult<List<UserEntity>>
}