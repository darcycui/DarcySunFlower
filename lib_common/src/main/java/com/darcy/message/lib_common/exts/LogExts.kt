package com.darcy.message.lib_common.exts

import com.elvishew.xlog.XLog

var FOR_TEST = false
const val LOG_TAG = "DarcyLog"
fun Any?.logD(message: String?, tag: String? = LOG_TAG) {
    if (FOR_TEST) {
        println("$tag $message")
        return
    }
    XLog.d("$tag $message")
}

fun Any?.logI(message: String?, tag: String? = LOG_TAG) {
    if (FOR_TEST) {
        println("$tag $message")
        return
    }
    XLog.i("$tag $message")
}

fun Any?.logV(message: String?, tag: String? = LOG_TAG) {
    if (FOR_TEST) {
        println("$tag $message")
        return
    }
    XLog.v("$tag $message")
}

fun Any?.logW(message: String?, tag: String? = LOG_TAG) {
    if (FOR_TEST) {
        println("$tag $message")
        return
    }
    XLog.w("$tag $message")
}

fun Any?.logE(message: String?, tag: String? = LOG_TAG) {
    if (FOR_TEST) {
        println("$tag $message")
        return
    }
    XLog.e("$tag $message")
}

