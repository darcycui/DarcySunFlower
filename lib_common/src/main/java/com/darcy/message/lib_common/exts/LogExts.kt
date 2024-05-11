package com.darcy.message.lib_common.exts

import com.elvishew.xlog.XLog

const val LOG_TAG = "DarcyLog"
fun Any?.logD(tag: String? = LOG_TAG, message: String?) {
    XLog.d("$tag $message")
}

fun Any?.logI(tag: String? = LOG_TAG, message: String?) {
    XLog.i("$tag $message")
}

fun Any?.logV(tag: String? = LOG_TAG, message: String?) {
    XLog.v("$tag $message")
}

fun Any?.logW(tag: String? = LOG_TAG, message: String?) {
    XLog.w("$tag $message")
}

fun Any?.logE(tag: String? = LOG_TAG, message: String?) {
    XLog.e("$tag $message")
}

fun Exception.print() {
    logE(message = "fail with Exception: ${this.stackTraceToString()}")
    this.printStackTrace()
}
