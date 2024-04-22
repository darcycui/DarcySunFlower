package com.darcy.message.lib_http.exts

fun Map<String, String>.toUrlEncodedString(): String {
    return this.entries.joinToString("&") {(key,value)->
        "$key=$value"
    }
}