package com.darcy.message.lib_http.factory

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy

object GsonFactory {
    fun createGson(): Gson {
        return GsonBuilder()
            // darcyRefactor: 这里设置为 LONG_OR_DOUBLE 指定数字类型转换为 long 或 double
            //  (默认 DOUBLE 数字类型转换为 double)
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            // darcyRefactor: 当使用 Class 对象作为解析类型时 Gson 自定义的解析器生效
            //  而使用 TypeToken 时 自定义解析器无效 使用默认解析器
//            .registerTypeAdapter(Long::class.java, LongTypeAdapter())
//            .registerTypeAdapter(Long::class.javaPrimitiveType, LongTypeAdapter())
            .create()
    }
}