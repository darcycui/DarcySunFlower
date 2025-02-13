package com.darcy.message.lib_task

import com.darcy.message.lib_task.manager.ITaskManager
import com.darcy.message.lib_task.manager.TaskManagerImpl
import com.darcy.message.lib_task.task.ITask

object TaskHelper: ITaskFacade {
    private val taskManager: ITaskManager = TaskManagerImpl
    override fun addTask(task: ITask) {
        taskManager.addTask(task)
    }

    override fun addAllTaskParallelAllOf(taskList: List<ITask>) {
        taskManager.addAllTaskParallelAllOf(taskList)
    }

    override fun addAllTaskParallelAnyOf(taskList: List<ITask>) {
        taskManager.addAllTaskParallelAnyOf(taskList)
    }

    override fun addAllTaskSerial(taskList: List<ITask>) {
        taskManager.addAllTaskSerial(taskList)
    }

    override fun cancelTask(task: ITask) {
        taskManager.cancelTask(task)
    }

    override fun cancelTasks(taskList: List<ITask>) {
        taskManager.cancelTasks(taskList)
    }

    override fun clear() {
        taskManager.clear()
    }

    override fun enqueue(taskList: List<ITask>) {
        taskManager.enqueue(taskList)
    }
}