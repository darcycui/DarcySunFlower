package com.darcy.message.lib_task.util

object ThreadUtil {
    fun sleep(time: Long) {
        try {
            Thread.sleep(time)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}