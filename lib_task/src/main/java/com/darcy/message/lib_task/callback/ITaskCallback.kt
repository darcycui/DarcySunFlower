package com.darcy.message.lib_task.callback

import com.darcy.message.lib_task.task.ITask
import com.darcy.message.lib_task.tracker.TaskStatus

fun interface ITaskCallback {
    fun onTaskStatusChanged(task: ITask, status: TaskStatus)
}