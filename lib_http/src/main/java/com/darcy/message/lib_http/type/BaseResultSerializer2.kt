package com.darcy.message.lib_http.type

import com.darcy.message.lib_http.entity.base.BaseResult
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class BaseResultSerializer2<T>(
    private val dataSerializer: KSerializer<T>
) : KSerializer<BaseResult<T>> {
    override val descriptor: SerialDescriptor =
        SerialDescriptor("my.app.BaseResult", dataSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: BaseResult<T>) {
        val surrogate = BaseResultSurrogate(
            value.resultCode,
            value.errorCode,
            value.reason,
            value.result
        )
        encoder.encodeSerializableValue(BaseResultSurrogate.serializer(dataSerializer), surrogate)
    }

    override fun deserialize(decoder: Decoder): BaseResult<T> {
        val surrogate = decoder.decodeSerializableValue(BaseResultSurrogate.serializer(dataSerializer))
        return BaseResult(
            resultCode = surrogate.resultCode,
            errorCode = surrogate.errorCode,
            reason = surrogate.reason,
            result = surrogate.result
        )
    }
}

@InternalSerializationApi
@Serializable
@SerialName("BaseResult")
private class BaseResultSurrogate<T>(
    @SerializedName("resultcode")
    @SerialName("resultcode")
    val resultCode: Int = -1,

    @SerializedName("error_code")
    @SerialName("error_code")
    val errorCode: Int = -1,

    val reason: String = "",

    val result: T
) {
}