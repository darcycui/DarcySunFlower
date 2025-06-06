package com.darcy.message.lib_http.exts

import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.Parameters

fun Map<String, String>.toFormDataContent(): FormDataContent {
    val map = this
    // create parameters builder
    val parameters = Parameters.Companion.build {
        map.forEach {
            append(it.key, it.value)
        }
    }
    return FormDataContent(parameters)
}