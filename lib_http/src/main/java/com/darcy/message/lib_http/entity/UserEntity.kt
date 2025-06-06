package com.darcy.message.lib_http.entity

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

// @OptIn 提示当前非稳定API
@OptIn(InternalSerializationApi::class)
@Serializable
data class UserEntity(
    val id: Long = -1L,
    val name: String = "",
){
    override fun toString(): String {
        return "UserEntity(id=$id, name='$name')"
    }
}