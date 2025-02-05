package com.darcy.message.lib_task.engine

import com.darcy.message.lib_task.task.ITask

interface ITaskEngine {
    /**
     * 执行一个任务
     */
    fun execute(task: ITask)

    /**
     * 多个任务串行执行
     */
    fun executeAllInSerial(taskList: List<ITask>)

    /**
     * 多个任务并行执行
     */
    fun executeAllInParallel(taskList: List<ITask>)

    /**
     * 添加任务
     */
    fun enqueue(taskList: List<ITask>)
}