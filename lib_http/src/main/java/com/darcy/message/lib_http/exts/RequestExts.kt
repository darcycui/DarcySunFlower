package com.darcy.message.lib_http.exts

import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.Parameters
import okhttp3.FormBody

fun Map<String, String>.toFormBody(): FormBody {
    // create formBody builder
    val formBody = FormBody.Builder()
    // traversal map to add params
    if (this.isEmpty()) return formBody.build()
    this.forEach { (k, v) ->
        if (v.isEmpty()) return@forEach
        formBody.add(k, v)
    }
    return formBody.build()
}

fun Map<String, String>.toFormDataContent(): FormDataContent {
    val map = this
    // create parameters builder
    val parameters = Parameters.build {
        map.forEach {
            append(it.key, it.value)
        }
    }
    return FormDataContent(parameters)
}