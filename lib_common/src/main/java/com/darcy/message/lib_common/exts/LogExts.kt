package com.darcy.message.lib_common.exts

import com.elvishew.xlog.XLog

const val LOG_TAG = " DarcyLog "
fun Any?.logD(tag: String? = LOG_TAG, message: String?) {
    XLog.d((tag ?: LOG_TAG) + LOG_TAG + message)
}

fun Any?.logI(tag: String? = LOG_TAG, message: String?) {
    XLog.i((tag ?: LOG_TAG) + LOG_TAG + message)
}

fun Any?.logV(tag: String? = LOG_TAG, message: String?) {
    XLog.v((tag ?: LOG_TAG) + LOG_TAG + message)
}

fun Any?.logW(tag: String? = LOG_TAG, message: String?) {
    XLog.w((tag ?: LOG_TAG) + LOG_TAG + message)
}

fun Any?.logE(tag: String? = LOG_TAG, message: String?) {
    XLog.e((tag ?: LOG_TAG) + LOG_TAG + message)
}

fun Exception.print() {
    logE(message = "fail with Exception: $this")
    this.printStackTrace()
}
