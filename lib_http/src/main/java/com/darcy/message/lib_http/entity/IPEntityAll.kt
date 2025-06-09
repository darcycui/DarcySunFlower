package com.darcy.message.lib_http.entity

import com.darcy.message.lib_http.entity.base.IEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// @OptIn 提示当前非稳定API
@OptIn(InternalSerializationApi::class)
@Serializable
data class IPEntityAll(
    @SerializedName("resultcode")
    @SerialName("resultcode")
    val resultcode: Int = -1,

    @SerializedName("error_code")
    @SerialName("error_code")
    val errorCode: Int = -1,

    val reason: String = "",

    val result: IPEntity
): IEntity