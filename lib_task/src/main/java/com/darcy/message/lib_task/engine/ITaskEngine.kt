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
     * 多个任务并行执行 所有任务必须成功才算成功
     */
    fun executeAllInParallelAllOf(taskList: List<ITask>)

    /**
     * 多个任务并行执行 任意一个成功算成功
     */
    fun executeAllInParallelAnyOf(taskList: List<ITask>)

    /**
     * 取消任务
     */
    fun cancel(task: ITask)

    /**
     * 取消多个任务
     */
    fun cancelTasks(taskList: List<ITask>)


    /**
     * 取消所有任务
     */
    fun clear()

    /**
     * 添加任务
     */
    fun enqueue(taskList: List<ITask>)
}