package com.darcy.message.lib_http.exts

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