package com.darcy.lib_download.actions.unziper

import com.darcy.lib_download.actions.AppInstallTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object UnzipManager {
    suspend fun startUnzip(task: AppInstallTask) {
        withContext(Dispatchers.IO) {
            task.unzipListener?.onStart(task)
            repeat(100) {
                val progress = (it + 1).toDouble() / 100
                delay(50)
                task.unzipListener?.onProgress(task, progress)
            }
            task.unzipListener?.onFinish(task)
        }
    }
}