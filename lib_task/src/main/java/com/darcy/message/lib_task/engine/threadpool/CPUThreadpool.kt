package com.darcy.message.lib_task.engine.threadpool

import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_task.thread.TaskThreadFactory
import com.darcy.message.lib_task.tracker.ITaskTracker
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

class CPUThreadpool : IThreadPool{
    companion object {
        private val TAG = CPUThreadpool::class.java.simpleName
    }

    private val corePoolSize = max(2, min(Runtime.getRuntime().availableProcessors() - 1, 4))
    private val maxPoolSize = corePoolSize * 2
    private val keepAliveTime = 60L
    private val workQueue: LinkedBlockingQueue<Runnable> = LinkedBlockingQueue(10)
    private val threadFactory = TaskThreadFactory()
    private val rejectHandler: RejectedExecutionHandler = object : RejectedExecutionHandler {
        override fun rejectedExecution(r: Runnable?, e: ThreadPoolExecutor?) {
            logE("$TAG rejectHandler")
            r?.run()
        }
    }
    private lateinit var taskTracker: ITaskTracker

    private val executor: ExecutorService = ThreadPoolExecutor(
        corePoolSize, maxPoolSize,
        keepAliveTime, TimeUnit.SECONDS,
        workQueue, threadFactory, rejectHandler
    )

    override fun getExecutor(): ExecutorService {
        return executor
    }
}