package com.darcy.message.lib_task.callback.impl

import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_common.exts.logW
import com.darcy.message.lib_task.callback.ITaskCallback
import com.darcy.message.lib_task.task.ITask
import com.darcy.message.lib_task.tracker.TaskStatus

class TaskLogCallback: ITaskCallback {
    override fun onTaskStatusChanged(task: ITask, status: TaskStatus) {
        when (status) {
            is TaskStatus.Pending -> {
                logV("${task.getTaskId()} pending callback")
            }
            is TaskStatus.Running -> {
                logV("${task.getTaskId()} running callback")
            }
            is TaskStatus.Canceled -> {
                logW("${task.getTaskId()} canceled callback")
            }
            is TaskStatus.Success -> {
                logI("${task.getTaskId()} success callback-->${status.result}")
            }
            is TaskStatus.Error -> {
                logE("${task.getTaskId()} error callback: ${status.cause}")
            }
            is TaskStatus.Unknown -> {
                logE("${task.getTaskId()} unknown callback")
            }
        }
    }
}