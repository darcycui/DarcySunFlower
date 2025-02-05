package com.darcy.message.lib_task.task.impl

import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_task.callback.ITaskCallback
import com.darcy.message.lib_task.callback.impl.TaskLogCallback
import com.darcy.message.lib_task.dispatcher.TaskDispatcher
import com.darcy.message.lib_task.task.ITask
import com.darcy.message.lib_task.task.TaskId
import com.darcy.message.lib_task.util.ThreadUtil
import java.util.concurrent.atomic.AtomicInteger

class TestTask2 : ITask {
    companion object {
        private val TAG = TestTask2::class.java.simpleName
        private var taskNumber: AtomicInteger = AtomicInteger(0)
    }

    private val taskId = "$TAG-${taskNumber.incrementAndGet()}"

    override fun execute(): Result<String> {
        logV("$taskId execute start... ${Thread.currentThread()}")
        ThreadUtil.sleep(1_000)
        logV("$taskId execute end...")
        val a = 1 / 0
        return Result.success("$taskId success")
    }

    override fun getTaskId(): TaskId {
        return taskId
    }

    override fun getTaskName(): String {
        return TAG
    }

    override fun getTaskCallback(): ITaskCallback? {
        return TaskLogCallback()
    }

    override fun getDispatcher(): TaskDispatcher {
        return TaskDispatcher.IO
    }


}