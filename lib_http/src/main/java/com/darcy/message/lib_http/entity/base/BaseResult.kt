package com.darcy.message.lib_http.entity.base

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// @OptIn 提示当前非稳定API
@OptIn(InternalSerializationApi::class)
@Serializable
data class BaseResult<T>(
    @SerializedName("resultcode")
    @SerialName("resultcode")
    val resultCode: Int = -1,

    @SerializedName("error_code")
    @SerialName("error_code")
    val errorCode: Int = -1,

    val reason: String = "",

    val result: T
) {
    override fun toString(): String {
        return "BaseResult(resultCode=$resultCode, errorCode=$errorCode, reason='$reason', result=$result)"
    }
}
