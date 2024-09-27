package com.darcy.message.lib_common.exts

import com.elvishew.xlog.XLog

const val LOG_TAG = "DarcyLog"
fun Any?.logD(message: String?, tag: String? = LOG_TAG) {
    XLog.d("$tag $message")
}

fun Any?.logI(message: String?, tag: String? = LOG_TAG) {
    XLog.i("$tag $message")
}

fun Any?.logV(message: String?, tag: String? = LOG_TAG) {
    XLog.v("$tag $message")
}

fun Any?.logW(message: String?, tag: String? = LOG_TAG) {
    XLog.w("$tag $message")
}

fun Any?.logE(message: String?, tag: String? = LOG_TAG) {
    XLog.e("$tag $message")
}

