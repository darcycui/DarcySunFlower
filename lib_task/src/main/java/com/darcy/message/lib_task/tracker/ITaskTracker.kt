package com.darcy.message.lib_task.tracker

import com.darcy.message.lib_task.task.TaskId

interface ITaskTracker {
    fun setTaskStatus(taskId: TaskId, status: TaskStatus)
    fun getTaskStatus(taskId: TaskId): TaskStatus
}

sealed class TaskStatus {
    data object Unknown : TaskStatus()
    data object Pending : TaskStatus()
    data object Running : TaskStatus()
    data object Canceled : TaskStatus()
    data class Success(val result: String) : TaskStatus()
    data class Error(val cause: Throwable) : TaskStatus()
}