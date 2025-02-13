package com.darcy.message.lib_task.task.impl

import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_task.callback.ITaskCallback
import com.darcy.message.lib_task.dispatcher.TaskDispatcher
import com.darcy.message.lib_task.exception.CancelException
import com.darcy.message.lib_task.task.ITask
import com.darcy.message.lib_task.task.TaskId
import com.darcy.message.lib_task.tracker.TaskStatus
import com.darcy.message.lib_task.util.ThreadUtil
import java.util.concurrent.atomic.AtomicInteger

/**
 * 模拟task 延迟3秒后执行完毕
 */
class TestTask3 : ITask {
    companion object {
        private val TAG = TestTask3::class.java.simpleName
        private var taskNumber: AtomicInteger = AtomicInteger(0)
    }

    private val taskId = "$TAG-${taskNumber.incrementAndGet()}"
    private var taskStatus : TaskStatus = TaskStatus.Unknown

    override fun execute(): Result<String> {
        logV("$taskId execute start... ${Thread.currentThread()}")
        ThreadUtil.sleep(3_000)
        if (taskStatus == TaskStatus.Canceled) {
            logV("$taskId canceled")
            return Result.failure(CancelException("$taskId canceled"))
        }
        logV("$taskId execute end...")
        return Result.success("$taskId success")
    }

    override fun getTaskId(): TaskId {
        return taskId
    }

    override fun getTaskName(): String {
        return TAG
    }

    override fun getTaskCallback(): ITaskCallback? {
        return object:ITaskCallback{
            override fun onTaskStatusChanged(task: ITask, status: TaskStatus) {
                logV("$taskId status changed $status")
                taskStatus = status
            }
        }
    }

    override fun getDispatcher(): TaskDispatcher {
        return TaskDispatcher.IO
    }


}