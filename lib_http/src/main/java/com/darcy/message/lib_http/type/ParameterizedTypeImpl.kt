package com.darcy.message.lib_http.type

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ParameterizedTypeImpl(private val raw: Class<*>, args: Array<Type?>?) : ParameterizedType {
    private val args: Array<Type?> = args ?: arrayOfNulls<Type>(0)

    override fun getActualTypeArguments(): Array<Type?> {
        return args
    }

    override fun getRawType(): Type {
        return raw
    }

    override fun getOwnerType(): Type? {
        return null
    }
}