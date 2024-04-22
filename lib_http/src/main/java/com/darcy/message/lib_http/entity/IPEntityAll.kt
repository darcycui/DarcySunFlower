package com.darcy.message.lib_http.entity

import kotlinx.serialization.Serializable

@Serializable
data class IPEntityAll(
    val resultcode: String = "",
    val error_code: String = "",
    val reason: String = "",
    val result: IPEntity
)