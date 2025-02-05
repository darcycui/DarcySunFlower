package com.darcy.message.lib_task.thread

import com.darcy.message.lib_common.exts.logD

data class TaskThread(val name1: String, val r: Runnable) : Thread(name1) {
    override fun run() {
        logD("$name1 run...")
        r.run()
    }
    override fun toString(): String {
        return "TaskThread(name1='$name1', id=$id)"
    }
}