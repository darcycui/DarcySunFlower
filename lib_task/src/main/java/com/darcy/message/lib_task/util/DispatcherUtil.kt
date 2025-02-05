package com.darcy.message.lib_task.util

import com.darcy.message.lib_task.task.ITask

object DispatcherUtil {
    /**
     * 判断任务是否都来自同一个调度器
     */
    fun areDispatchersTheSame(taskList: List<ITask>): Boolean {
        if (taskList.isEmpty()) {
            return false
        }
        val firstDispatcher = taskList.first().getDispatcher()
        // all task dispatcher must be same
        return taskList.all { it.getDispatcher() == firstDispatcher }
    }
}