package com.darcy.message.lib_task.manager

import com.darcy.message.lib_task.task.ITask

interface ITaskManager {
    fun addTask(task: ITask)
    fun addAllTaskParallelAllOf(taskList: List<ITask>)
    fun addAllTaskParallelAnyOf(taskList: List<ITask>)
    fun addAllTaskSerial(taskList: List<ITask>)
    fun cancelTask(task: ITask)
    fun cancelTasks(taskList: List<ITask>)
    fun clear()

    fun enqueue(taskList: List<ITask>)

}