package com.darcy.message.lib_http.type

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateAsSimpleTextSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("my.app.DateAsSimpleText", PrimitiveKind.LONG)
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).apply {
        // Here we explicitly set time zone to UTC so output for this sample remains locale-independent.
        // Depending on your needs, you may have to adjust or remove this line.
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun serialize(encoder: Encoder, value: Date) =
        encoder.encodeString(format.format(value))

    override fun deserialize(decoder: Decoder): Date =
        format.parse(decoder.decodeString()) ?: Date()
}