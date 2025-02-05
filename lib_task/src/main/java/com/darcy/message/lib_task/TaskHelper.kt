package com.darcy.message.lib_task

import com.darcy.message.lib_task.manager.ITaskManager
import com.darcy.message.lib_task.manager.TaskManagerImpl
import com.darcy.message.lib_task.task.ITask

object TaskHelper: ITaskFacade {
    private val taskManager: ITaskManager = TaskManagerImpl
    override fun addTask(task: ITask) {
        taskManager.addTask(task)
    }

    override fun addAllTaskParallel(taskList: List<ITask>) {
        taskManager.addAllTaskParallel(taskList)
    }

    override fun addAllTaskSerial(taskList: List<ITask>) {
        taskManager.addAllTaskSerial(taskList)
    }

    override fun cancelTask(task: ITask) {
        taskManager.cancelTask(task)
    }

    override fun cancelAllTask() {
        taskManager.cancelAllTask()
    }

    override fun enqueue(taskList: List<ITask>) {
        taskManager.enqueue(taskList)
    }
}