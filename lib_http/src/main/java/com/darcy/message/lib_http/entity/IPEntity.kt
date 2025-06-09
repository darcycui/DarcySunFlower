package com.darcy.message.lib_http.entity

import com.darcy.message.lib_http.entity.base.IEntity
import com.darcy.message.lib_http.type.LocalDateTimeSerializer
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

// @OptIn 提示当前非稳定API
@OptIn(InternalSerializationApi::class)
@Serializable
data class IPEntity(
    @SerialName("City")
    @SerializedName("City")
    val city: String = "",
    @SerialName("Country")
    @SerializedName("Country")
    val country: String = "",
    @SerialName("District")
    @SerializedName("District")
    val district: String = "",
    @SerialName("Isp")
    @SerializedName("Isp")
    val isp: String = "",
    @SerialName("Province")
    @SerializedName("Province")
    val province: String = "",

    @Serializable(with = LocalDateTimeSerializer::class)
    val time: LocalDateTime = LocalDateTime.now().also {
        println("time: $it")
    },
): IEntity {
    override fun toString(): String {
        return "IPEntity(city='$city', country='$country', district='$district', isp='$isp', province='$province', time=$time)"
    }
}