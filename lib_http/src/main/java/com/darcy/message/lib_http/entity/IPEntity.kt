package com.darcy.message.lib_http.entity

import kotlinx.serialization.Serializable

@Serializable
data class IPEntity(
    val City: String = "",
    val Country: String = "",
    val District: String = "",
    val Isp: String = "",
    val Province: String = ""
)