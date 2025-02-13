package com.darcy.message.lib_task.manager

import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_task.engine.ITaskEngine
import com.darcy.message.lib_task.engine.ThreadpoolTaskEngine
import com.darcy.message.lib_task.task.ITask
import com.darcy.message.lib_task.tracker.ITaskTracker
import com.darcy.message.lib_task.tracker.TaskStatus
import com.darcy.message.lib_task.tracker.impl.TaskTrackerImpl
import java.util.Collections

object TaskManagerImpl : ITaskManager {
    private val TAG: String = TaskManagerImpl.javaClass.simpleName
    private val taskList: MutableList<ITask> = Collections.synchronizedList(mutableListOf())
    private val taskTracker: ITaskTracker = TaskTrackerImpl()

        private val iTaskEngine: ITaskEngine = ThreadpoolTaskEngine.getInstance(taskTracker)
//    private val iTaskEngine: ITaskEngine = CoroutineTaskEngine.getInstance(taskTracker)

    @Synchronized
    override fun addTask(task: ITask) {
        logD("$TAG addTask: $task")
        taskList.add(task)
        taskTracker.setTaskStatus(task, TaskStatus.Pending)
        task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Pending)
        iTaskEngine.execute(task)
    }

    @Synchronized
    override fun addAllTaskParallelAllOf(taskList: List<ITask>) {
        iTaskEngine.executeAllInParallelAllOf(taskList)
    }

    override fun addAllTaskParallelAnyOf(taskList: List<ITask>) {
        iTaskEngine.executeAllInParallelAnyOf(taskList)
    }

    override fun addAllTaskSerial(taskList: List<ITask>) {
        iTaskEngine.executeAllInSerial(taskList)
    }

    @Synchronized
    override fun cancelTask(task: ITask) {
        taskList.remove(task)
        taskTracker.setTaskStatus(task, TaskStatus.Canceled)
        task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Canceled)
    }

    override fun cancelTasks(taskList: List<ITask>) {
        taskList.forEach { task->
            taskTracker.setTaskStatus(task, TaskStatus.Canceled)
            task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Canceled)
        }
    }

    @Synchronized

    override fun clear() {
        cancelTasks(this.taskList)
        this.taskList.clear()
    }

    @Synchronized
    override fun enqueue(taskList: List<ITask>) {
        iTaskEngine.enqueue(taskList)
    }
}