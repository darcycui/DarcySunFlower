package com.darcy.message.lib_http.factory

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

object JsonFactory {
    fun createJson(): Json {
        return Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            coerceInputValues = true
            serializersModule = SerializersModule {
                // 注册BaseResult的上下文序列化器 KSerializer
            }
        }
    }
}