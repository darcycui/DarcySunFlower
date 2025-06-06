package com.darcy.message.lib_http.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val province: String = ""
) {
    override fun toString(): String {
        return "IPEntity(city='$city', country='$country', district='$district', isp='$isp', province='$province')"
    }
}