package com.darcy.message.lib_task.util

import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI

object ResultUtil {
    fun parseResultStr(tag: String, result: Result<*>): String {
        return result.onSuccess {
            logI("$tag execute success")
        }.onFailure {
            logE("$tag execute failure")
        }.getOrElse { "null" }.toString().also {
            logD("$tag parse string==>$it")
        }
    }
}