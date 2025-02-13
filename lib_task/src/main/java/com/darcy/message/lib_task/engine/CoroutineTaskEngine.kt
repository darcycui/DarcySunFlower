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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

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
            if (taskTracker.getTaskStatus(task) == TaskStatus.Canceled) {
                logD("$TAG task:${task} is canceled")
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

    override fun executeAllInParallelAllOf(taskList: List<ITask>) {
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
                logV("$TAG allOf Parallel result=$result")
            }
            logV("$TAG allOf Parallel:All tasks completed.")
        }
    }

    override fun executeAllInParallelAnyOf(taskList: List<ITask>) {
        scope.launch {
            val suspendTasks = taskList.map { task ->
                suspend {
                    runTask(task)
                }
            }
            val deferredList = suspendTasks.map { suspendTask ->
                scope.async { suspendTask.invoke() }
            }
            var targetIndex = -1
            val result = select<String> {
                // darcyRefactor: 带索引的 forEach
                deferredList.withIndex().forEach { (index,deferred) ->
                    deferred.onAwait { it ->
                        targetIndex = index
                        it.getOrElse { throw it }
                    }
                }
            }
            logV("$TAG anyOf Parallel result=$result")
            logV("$TAG anyOf Parallel:All tasks completed.")
            deferredList.forEach {
                logD("$TAG anyOf Parallel cancel $it ${it.isCompleted}")
                if (it.isCompleted.not()){
                    logD("$TAG anyOf Parallel cancel $it")
                    it.cancel()
                }
            }
            taskList.withIndex().forEach { (index, task) ->
                if (index != targetIndex) {
                    cancel(task)
                }
            }
            this.cancel()
        }
    }

    override fun cancel(task: ITask) {
        taskTracker.setTaskStatus(task, TaskStatus.Canceled)
        task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Canceled)
    }

    override fun cancelTasks(taskList: List<ITask>) {
        taskList.forEach {
            cancel(it)
        }
    }

    override fun clear() {
    }

    override fun enqueue(taskList: List<ITask>) {
        TODO("Not yet implemented")
    }

    private fun runTask(task: ITask): Result<String> {
        return kotlin.runCatching {
            taskTracker.setTaskStatus(task, TaskStatus.Running)
            task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Running)
            val result = task.execute()
            ResultUtil.parseResultStr(task.getTaskId(), result)
        }.onFailure {
            logE("$TAG execute error:${it.message}")
            taskTracker.setTaskStatus(task, TaskStatus.Error(it))
            task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Error(it))
        }.onSuccess {
            if (taskTracker.getTaskStatus(task) == TaskStatus.Canceled){
                logD("$TAG current task ${task.getTaskId()} is canceled, ignore success status.")
                return@onSuccess
            }
            taskTracker.setTaskStatus(task, TaskStatus.Success(it))
            task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Success(it))
        }
    }
}