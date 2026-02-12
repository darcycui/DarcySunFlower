package com.darcy.lib_download.utils


fun Float.formatByDigits(digits: Int = 2): String {
    return String.format("%.${digits}f", this)
}

fun Double.formatByDigits(digits: Int = 2): String {
    return String.format("%.${digits}f", this)
}