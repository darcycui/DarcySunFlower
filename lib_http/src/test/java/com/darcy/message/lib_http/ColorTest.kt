package com.darcy.message.lib_http

import com.darcy.message.lib_http.exts.parser.kotlinxJson
import com.darcy.message.lib_http.type.Color
import org.junit.Test

class ColorTest {
    @Test
    fun test() {
        val green = Color(0x00ff00)
        println(kotlinxJson.encodeToString(green))
    }
}