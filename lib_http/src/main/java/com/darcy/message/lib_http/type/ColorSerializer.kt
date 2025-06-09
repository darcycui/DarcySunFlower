package com.darcy.message.lib_http.type

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ColorSerializer : KSerializer<Color> {
    // Serial names of descriptors should be unique, so we cannot use ColorSurrogate.serializer().descriptor directly
    override val descriptor: SerialDescriptor =
        SerialDescriptor("my.app.Color", ColorSurrogate.serializer().descriptor)

    override fun serialize(encoder: Encoder, value: Color) {
        val surrogate = ColorSurrogate(
            (value.rgb shr 16) and 0xff,
            (value.rgb shr 8) and 0xff,
            value.rgb and 0xff
        )
        encoder.encodeSerializableValue(ColorSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): Color {
        val surrogate = decoder.decodeSerializableValue(ColorSurrogate.serializer())
        return Color((surrogate.r shl 16) or (surrogate.g shl 8) or surrogate.b)
    }
}

@InternalSerializationApi
@Serializable
@SerialName("Color")
private class ColorSurrogate(val r: Int, val g: Int, val b: Int) {
    init {
        require(r in 0..255 && g in 0..255 && b in 0..255)
    }
}

@Serializable(with = ColorSerializer::class)
class Color(val rgb: Int)