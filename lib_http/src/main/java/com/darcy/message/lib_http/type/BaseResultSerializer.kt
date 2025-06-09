package com.darcy.message.lib_http.type

import com.darcy.message.lib_http.entity.base.BaseResult
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class BaseResultSerializer<T>(
    private val dataSerializer: KSerializer<T>
) : KSerializer<BaseResult<T>> {
    override val descriptor: SerialDescriptor =
        SerialDescriptor("my.app.BaseResult", dataSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: BaseResult<T>) =
        dataSerializer.serialize(encoder, value.result)

    override fun deserialize(decoder: Decoder) = BaseResult(
        resultCode = -1,
        errorCode = -1,
        reason = "",
        result = dataSerializer.deserialize(decoder)
    )
}