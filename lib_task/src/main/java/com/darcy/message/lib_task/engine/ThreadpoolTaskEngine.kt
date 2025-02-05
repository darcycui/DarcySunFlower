package com.darcy.message.lib_task.engine

import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_task.dispatcher.TaskDispatcher
import com.darcy.message.lib_task.engine.threadpool.CPUThreadpool
import com.darcy.message.lib_task.engine.threadpool.IOThreadpool
import com.darcy.message.lib_task.exception.RunTaskException
import com.darcy.message.lib_task.task.ITask
import com.darcy.message.lib_task.tracker.ITaskTracker
import com.darcy.message.lib_task.tracker.TaskStatus
import com.darcy.message.lib_task.util.DispatcherUtil
import com.darcy.message.lib_task.util.ResultUtil
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors


class ThreadpoolTaskEngine : ITaskEngine {
    companion object {
        private val TAG = ThreadpoolTaskEngine::class.java.simpleName
        private var instance: ThreadpoolTaskEngine? = null
        fun getInstance(taskTracker: ITaskTracker): ThreadpoolTaskEngine {
            if (instance == null) {
                instance = ThreadpoolTaskEngine()
            }
            instance?.taskTracker = taskTracker
            return instance!!
        }
    }

    private lateinit var taskTracker: ITaskTracker
    private val serialChainStatusMap: ConcurrentHashMap<List<ITask>, Boolean> = ConcurrentHashMap()

    private val executorCPU: ExecutorService = CPUThreadpool().getExecutor()
    private val executorIO: ExecutorService = IOThreadpool().getExecutor()

    override fun execute(task: ITask) {
        val executor = getRealExecutor(listOf(task))
        executor?.execute {
            logD("$TAG task:${task.getTaskId()} dispatcher:${task.getDispatcher()}")
            if (taskTracker.getTaskStatus(task.getTaskId()) == TaskStatus.Canceled) {
                logD("$TAG task:${task.getTaskId()} is canceled")
                return@execute
            }
            runTask(task)
        } ?: {
            logE("$TAG task:${task.getTaskId()} executor is null")
        }
    }

    override fun executeAllInSerial(taskList: List<ITask>) {
        if (taskList.isEmpty()) {
            logE("$TAG taskList is empty")
            return
        }
        if (!DispatcherUtil.areDispatchersTheSame(taskList)) {
            logE("$TAG taskList:${taskList.map { it.getDispatcher() }} dispatcher is not the same")
            return
        }
        val executor = getRealExecutor(taskList)
        if (executor == null) {
            logE("$TAG executeAllInSerial executor is null")
            return
        }
        serialChainStatusMap[taskList] = true
        var finalFuture: CompletableFuture<*> = CompletableFuture.completedFuture(null)
        for ((index, task) in taskList.withIndex()) {
            if (serialChainStatusMap[taskList] == false) {
                logE("$TAG There is a task error occurs. Stop the serialChain.")
                break
            }
            finalFuture = finalFuture.thenComposeAsync { prevResult ->
                logD("Serial: prevResult=$prevResult")
                CompletableFuture.supplyAsync({
                    runTask(task)
                }, executor)
                    .exceptionally {
                        serialChainStatusMap[taskList] = false
                        dealSerialChainError(index, taskList, it)
                        // 异常处理 返回Result.failure
                        Result.failure(it)
                    }.whenComplete { result, throwable ->
                        val string = result.getOrNull()
                        logD("Serial:whenComplete task completed. result=$string, Throwable=${throwable}")
                    }
            }
            finalFuture.get().also {
                logD(TAG, "Serial: get result: $it")
            }
        }
        finalFuture.thenApply {
            if (serialChainStatusMap[taskList] == true) {
                logD("Serial:All tasks completed.")
            } else {
                logD("Serial:Partial tasks completed.")
            }
        }
    }

    /**
     * 所有后续任务标记为失败
     */
    private fun dealSerialChainError(
        index: Int,
        taskList: List<ITask>,
        it: Throwable
    ) {
        for (i in index + 1 until taskList.size) {
            taskTracker.setTaskStatus(taskList[i].getTaskId(), TaskStatus.Error(it))
            taskList[i].getTaskCallback()?.onTaskStatusChanged(taskList[i], TaskStatus.Error(it))
        }
    }

    override fun executeAllInParallel(taskList: List<ITask>) {
        if (taskList.isEmpty()) {
            logE("$TAG taskList is empty")
            return
        }
        if (!DispatcherUtil.areDispatchersTheSame(taskList)) {
            logE("$TAG taskList:${taskList.map { it.getDispatcher() }} dispatcher is not the same")
            return
        }
        val executor = getRealExecutor(taskList)
        if (executor == null) {
            logE("$TAG executeAllInParallel executor is null")
            return
        }
        val allFutures = taskList.stream().map { task ->
            CompletableFuture.supplyAsync({
                runTask(task)
            }, executor)
        }.collect(Collectors.toList())

        CompletableFuture.allOf(*allFutures.toTypedArray()).thenApply {
            for (future in allFutures) {
                logD("Parallel future Result: ${future.get()}")
            }
        }.thenAccept {
            logD("Parallel:All tasks completed.")
        }
    }

    override fun enqueue(taskList: List<ITask>) {
        TODO("Not yet implemented")
    }

    private fun getRealExecutor(taskList: List<ITask>): ExecutorService? {
        return when (taskList.first().getDispatcher()) {
            TaskDispatcher.IO -> executorIO
            TaskDispatcher.CPU -> executorCPU
            else -> {
                logE("$TAG taskList:${taskList.map { it.getDispatcher() }} dispatcher is not supported")
                return null
            }
        }
    }

    private fun runTask(
        task: ITask,
        start: (() -> Unit)? = null,
        success: ((result: String) -> Unit)? = null,
        error: ((cause: RunTaskException) -> Unit)? = null,
    ): Result<String> {
        return kotlin.runCatching {
            start?.invoke()
            taskTracker.setTaskStatus(task.getTaskId(), TaskStatus.Running)
            task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Running)
            val result = task.execute()
            ResultUtil.parseResultStr(task.getTaskId(), result)
        }.onSuccess {
            success?.invoke(it)
            taskTracker.setTaskStatus(task.getTaskId(), TaskStatus.Success(it))
            task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Success(it))
        }.onFailure {
            val exception = RunTaskException(it)
            logE("$TAG execute error:$exception")
            error?.invoke(exception)
            taskTracker.setTaskStatus(task.getTaskId(), TaskStatus.Error(it))
            task.getTaskCallback()?.onTaskStatusChanged(task, TaskStatus.Error(it))
            throw exception
        }
    }
}
