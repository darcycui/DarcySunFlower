package com.darcy.message.lib_http

import com.darcy.message.lib_http.exts.parser.kotlinxJson
import com.darcy.message.lib_http.type.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.junit.Test
import java.time.LocalDateTime

@Serializable
data class LocalTimeProject(
    val name: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
)

class LocalDateSerializerTest {
    @Test
    fun test() {
        val data = kotlinxJson.encodeToString(LocalTimeProject("测试项目", LocalDateTime.now()))
        println(data)
    }
}