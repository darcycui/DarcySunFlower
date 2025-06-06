package com.darcy.message.lib_http.typeadapter

import com.darcy.message.lib_common.exts.logV
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * 定义 Long 类型适配器
 */
class LongTypeAdapter : TypeAdapter<Long>() {
    companion object {
        private val TAG = LongTypeAdapter::class.java.simpleName
    }

    override fun write(out: JsonWriter, value: Long?) {
        logV("$TAG write value: $value")
        println("$TAG write value: $value")
        // 如果value是null，则调用JsonWriter的nullValue方法
        if (value == null) {
            out.nullValue()
            return
        }
        // 将Long值以字符串的形式写出
        out.value(value.toString())
    }

    override fun read(reader: JsonReader): Long? {
        logV("$TAG write value:")
        println("$TAG write value:")
        return when (val token = reader.peek()) {
            JsonToken.NULL -> {
                reader.nextNull()
                null
            }
            JsonToken.NUMBER -> {
                reader.nextLong().also {
                    logV("$TAG read number: $it")
                }
            }
            JsonToken.STRING -> {
                // 注意：如果字符串为空，则返回null，否则尝试转换
                val stringValue = reader.nextString()
                if (stringValue.isBlank()) {
                    null
                } else {
                    stringValue.toLong().also {
                        logV("$TAG read string: $it")
                    }
                }
            }
            else -> {
                // 遇到其他类型，跳过（或者抛出异常，取决于你的需要）
                reader.skipValue()
                logV("$TAG read unexpected token: $token")
                null // 或者抛出异常
            }
        }
    }
}