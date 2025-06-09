package com.darcy.message.lib_http

import com.darcy.message.lib_http.exts.parser.kotlinxJson
import com.darcy.message.lib_http.type.Box
import kotlinx.serialization.Serializable
import org.junit.Test

@Serializable
data class BoxProject(val name: String)

class BoxTest {

    @Test
    fun test() {
        val box = Box(BoxProject("kotlinx.serialization"))
        val string = kotlinxJson.encodeToString(box)
        println(string)
        println(kotlinxJson.decodeFromString<Box<LocalTimeProject>>(string))
    }
}