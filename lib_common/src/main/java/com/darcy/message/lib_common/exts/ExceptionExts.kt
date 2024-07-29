package com.darcy.message.lib_common.exts

/**
 * print exception stacktrace as string
 */
fun Throwable.print() {
    logE(message = "fail with Exception: ${this.stackTraceToString()}")
    //this.printStackTrace()
}