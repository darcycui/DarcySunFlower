package com.darcy.message.lib_app_status.crash

import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logW
import com.darcy.message.lib_common.exts.print
import java.lang.Thread.UncaughtExceptionHandler

object CustomUncaughtExceptionHandler : UncaughtExceptionHandler {

    private val defaultExceptionHandler: UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        // 记录日志
        logE("CustomExceptionHandler Uncaught exception: ${ex.message}")
        ex.print()

        // 发送错误报告
        sendErrorReport(ex)

        // 调用默认的异常处理器
        defaultExceptionHandler?.uncaughtException(thread, ex)
    }

    private fun sendErrorReport(ex: Throwable) {
        // 实现发送错误报告的逻辑
        // 例如，可以将错误信息发送到服务器
        logW("CustomExceptionHandler Sending error report to server: ${ex.message}")
    }
}
