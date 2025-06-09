package com.darcy.message.lib_http

import com.darcy.message.lib_http.exts.parser.kotlinxJson
import com.darcy.message.lib_http.type.DateAsLongSerializer
import com.darcy.message.lib_http.type.DateAsSimpleTextSerializer
import kotlinx.serialization.Serializable
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

typealias DateAsText = @Serializable(DateAsSimpleTextSerializer::class) Date

typealias DateAsLong = @Serializable(DateAsLongSerializer::class) Date

@Serializable
class ProgrammingLanguage(val stableReleaseDate: DateAsText, val lastReleaseTimestamp: DateAsLong)

class DateKSerializerTest {

    @Test
    fun test() {
        val format = SimpleDateFormat("yyyy-MM-ddX", Locale.CHINA)
        val data = ProgrammingLanguage(format.parse("2016-02-15+00"), format.parse("2022-07-07+00"))
        println(kotlinxJson.encodeToString(data))
    }
}