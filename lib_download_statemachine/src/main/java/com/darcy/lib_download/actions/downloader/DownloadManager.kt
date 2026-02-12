package com.darcy.lib_download.actions.downloader

import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.message.lib_common.exts.logW
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object DownloadManager {
    private val downloadingMap = mutableMapOf<Int, AppInstallTask>()
    private val jobs = mutableMapOf<Int, Job>() // 存储每个任务的协程 job
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logW("下载任务异常: $throwable")
    }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)

    @Synchronized
    fun startDownload(task: AppInstallTask) {
        // 检测任务是否已存在
        if (downloadingMap.containsKey(task.itemBean.id)) {
            logW("${task.itemBean.id} 下载任务已存在, 无需重复下载")
            return
        }
        val job = scope.launch {
            downloadingMap[task.itemBean.id] = task
            task.listener?.onStart(task)
            val existProgress = task.itemBean.downloadingProgress
            var progress = existProgress
            while (progress < 1) {
                if (task.itemBean.isPaused) {
                    delay(1_000)
                    continue
                }
                progress += 0.01
                task.itemBean.downloadingProgress = progress
                delay(1_0)
                task.listener?.onProgress(task, progress)
            }
            task.listener?.onFinish(task)
            jobs.remove(task.itemBean.id)
            cancelDownload(task)
        }
        jobs[task.itemBean.id] = job
    }

    @Synchronized
    fun pauseDownload(task: AppInstallTask) {
        task.itemBean.isPaused = true
        jobs[task.itemBean.id]?.cancel() // 取消任务 job
        jobs.remove(task.itemBean.id)
        downloadingMap.remove(task.itemBean.id)
        task.listener?.onPause(task)
    }

    @Synchronized
    fun resumeDownload(task: AppInstallTask) {
        task.itemBean.isPaused = false
        task.listener?.onResume(task)
        // startDownload(task)
    }

    @Synchronized
    fun cancelDownload(task: AppInstallTask) {
        task.listener?.onCancel(task)
        downloadingMap.remove(task.itemBean.id)
    }
}