package com.darcy.message.lib_task.thread

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class TaskThreadFactory : ThreadFactory {
    companion object {
        private val threadNumber: AtomicInteger = AtomicInteger(0)
    }

    override fun newThread(r: Runnable): Thread {
        return TaskThread("TaskThread:${threadNumber.incrementAndGet()}", r)
    }
}