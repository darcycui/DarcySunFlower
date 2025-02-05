package com.darcy.message.lib_task.engine

import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_task.task.ITask
import com.darcy.message.lib_task.tracker.ITaskTracker
import com.darcy.message.lib_task.tracker.TaskStatus
import com.darcy.message.lib_task.util.ResultUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class CoroutineTaskEngine : ITaskEngine {
    companion object {
        private val TAG = CoroutineTaskEngine::class.java.simpleName
        private var instance: CoroutineTaskEngine? = null
        fun getInstance(taskTracker: ITaskTracker): CoroutineTaskEngine {
            if (instance == null) {
                instance = CoroutineTaskEngine()
            }
            instance?.taskTracker = taskTracker
            return instance!!
        }
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var taskTracker: ITaskTracker

    override fun execute(task: ITask) {
        scope.launch {
            if (taskTracker.getTaskStatus(task.getTaskId()) == TaskStatus.Canceled) {
                logD("$TAG task:${task.getTaskId()} is canceled")
                return@launch
            }
            runTask(task)
        }
    }

    override fun executeAllInSerial(taskList: List<ITask>) {
        scope.launch {
            taskList.forEach { task ->
                runTask(task)
            }
            logV("Serial:All tasks completed.")
        }

    }

    override fun executeAllInParallel(taskList: List<ITask>) {
        scope.launch {
            val suspendTasks = taskList.map { task ->
                suspend {
                    runTask(task)
                }
            }
            val deferredList = suspendTasks.map { suspendTask ->
                scope.async { suspendTask() }
            }
            val results = deferredList.awaitAll()
            results.forEach { result ->
                logV("$TAG Parallel result=$result")
            }
            logV("Parallel:All tasks completed.")
        }
    }

    override fun enqueue(taskList: List<ITask>) {
        TODO("Not yet implemented")
    }

    private fun runTask(task: ITask): Result<String> {
        return kotlin.runCatching {
            taskTracker.setTaskStatus(task.getTaskId(), TaskStatus.Running)
            task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Running)
            val result = task.execute()
            ResultUtil.parseResultStr(task.getTaskId(), result)
        }.onFailure {
            logE("$TAG execute error:${it.message}")
            taskTracker.setTaskStatus(task.getTaskId(), TaskStatus.Error(it))
            task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Error(it))
        }.onSuccess {
            taskTracker.setTaskStatus(task.getTaskId(), TaskStatus.Success(it))
            task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Success(it))
        }
    }
}