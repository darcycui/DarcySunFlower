package com.darcy.message.lib_task.tracker.impl

import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_task.task.ITask
import com.darcy.message.lib_task.tracker.ITaskTracker
import com.darcy.message.lib_task.tracker.TaskStatus
import java.util.concurrent.ConcurrentHashMap

class TaskTrackerImpl : ITaskTracker {
    companion object {
        private val TAG = TaskTrackerImpl::class.java.simpleName
    }
    private val taskStatusMap: ConcurrentHashMap<ITask, TaskStatus> = ConcurrentHashMap()
    override fun setTaskStatus(iTask: ITask, status: TaskStatus) {
        synchronized(this) {
            taskStatusMap[iTask] = status
        }
    }

    @Synchronized
    override fun getTaskStatus(iTask: ITask): TaskStatus {
        return taskStatusMap[iTask] ?: TaskStatus.Unknown
    }
}