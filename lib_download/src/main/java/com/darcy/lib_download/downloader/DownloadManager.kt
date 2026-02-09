package com.darcy.lib_download.downloader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object DownloadManager {
    suspend fun startDownload(task: DownloadTask) {
        withContext(Dispatchers.IO) {
            task.listener?.onStart(task)
            repeat(100) {
                val progress = (it + 1).toDouble() / 100
                delay(1_00)
                task.listener?.onProgress(task, progress)
            }
            task.listener?.onFinish(task)
        }
    }
}