package com.darcy.message.lib_task.tracker.impl

import com.darcy.message.lib_task.task.TaskId
import com.darcy.message.lib_task.tracker.ITaskTracker
import com.darcy.message.lib_task.tracker.TaskStatus
import java.util.concurrent.ConcurrentHashMap

class TaskTrackerImpl : ITaskTracker {
    private val taskStatusMap: ConcurrentHashMap<TaskId, TaskStatus> = ConcurrentHashMap()
    override fun setTaskStatus(taskId: TaskId, status: TaskStatus) {
        synchronized(this) {
            taskStatusMap[taskId] = status
        }
    }

    @Synchronized
    override fun getTaskStatus(taskId: TaskId): TaskStatus {
        return taskStatusMap[taskId] ?: TaskStatus.Unknown
    }
}