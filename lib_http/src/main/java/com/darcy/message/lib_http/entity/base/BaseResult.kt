package com.darcy.message.lib_http.entity.base

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

// @OptIn 提示当前非稳定API
@OptIn(InternalSerializationApi::class)
@Serializable
//@Serializable(with = BaseResultSerializer::class)
//@Serializable(with = BaseResultSerializer2::class)
data class BaseResult<T>(
    @SerializedName("resultcode")
    @SerialName("resultcode")
    val resultCode: Int = -1,

    @SerializedName("error_code")
    @SerialName("error_code")
    val errorCode: Int = -1,

    val reason: String = "",

    val result: T
) : IEntity {
    companion object {
        // darcyRefactor: 使用顶层序列化函数 serializer()
        fun <T> createSerializer(elementSerializer: KSerializer<T>): KSerializer<BaseResult<T>> {
            return serializer(elementSerializer)
        }

        // darcyRefactor: 构建集合序列化器 ListSerializer()、SetSerializer()、MapSerializer()
        fun <T> createSerializerList(elementSerializer: KSerializer<T>): KSerializer<BaseResult<List<T>>> {
            return serializer(ListSerializer(elementSerializer))
        }
    }
    override fun toString(): String {
        return "BaseResult(resultCode=$resultCode, errorCode=$errorCode, reason='$reason', result=$result)"
    }
}
