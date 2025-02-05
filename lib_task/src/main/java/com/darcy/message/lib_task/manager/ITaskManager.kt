package com.darcy.message.lib_task.manager

import com.darcy.message.lib_task.task.ITask

interface ITaskManager {
    fun addTask(task: ITask)
    fun addAllTaskParallel(taskList: List<ITask>)
    fun addAllTaskSerial(taskList: List<ITask>)
    fun cancelTask(task: ITask)
    fun cancelAllTask()

    fun enqueue(taskList: List<ITask>)

}