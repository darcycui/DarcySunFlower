package com.darcy.message.lib_task.task

import com.darcy.message.lib_task.callback.ITaskCallback
import com.darcy.message.lib_task.dispatcher.TaskDispatcher


typealias TaskId = String

interface ITask {
    fun execute(): Result<*>
    fun getTaskId(): TaskId
    fun getTaskName(): String
    fun getTaskCallback(): ITaskCallback?
    fun getDispatcher(): TaskDispatcher

}