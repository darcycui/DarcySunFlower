package com.darcy.message.lib_task.dispatcher

sealed class TaskDispatcher {
    data object CPU : TaskDispatcher()
    data object IO : TaskDispatcher()
    data object Main : TaskDispatcher()
}