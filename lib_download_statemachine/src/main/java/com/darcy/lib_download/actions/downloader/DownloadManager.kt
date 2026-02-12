package com.darcy.lib_download.actions.downloader

import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.lib_download.utils.formatByDigits
import com.darcy.message.lib_common.exts.logW
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object DownloadManager {
    private val downloadingMap = mutableMapOf<Int, AppInstallTask>()
    suspend fun startDownload(task: AppInstallTask) {
        if (downloadingMap.containsKey(task.itemBean.id)) {
            logW("${task.itemBean.id} 任务已存在, 无需重复下载")
            return
        }
        withContext(Dispatchers.IO) {
            downloadingMap[task.itemBean.id] = task
            task.listener?.onStart(task)
            val existProgress = (task.downloadingProgress * 100).formatByDigits().toDouble().toInt()
            repeat(100 - existProgress) {
                val progress = (it + 1 + existProgress).toDouble() / 100
                delay(1_00)
                task.listener?.onProgress(task, progress)
            }
            task.listener?.onFinish(task)
        }
    }

    fun pauseDownload(task: AppInstallTask) {
        task.listener?.onPause(task)
    }

    fun cancelDownload(task: AppInstallTask) {
        task.listener?.onCancel(task)
        downloadingMap.remove(task.itemBean.id)
    }
}