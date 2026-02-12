package com.darcy.lib_download.actions.installer

import com.darcy.lib_download.actions.AppInstallTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object InstallManager {
    suspend fun startInstall(task: AppInstallTask) {
        withContext(Dispatchers.IO) {
            task.installListener?.onStart(task)
            repeat(100) {
                val progress = (it + 1).toDouble() / 100
                delay(80)
                task.installListener?.onProgress(task, progress)
            }
            task.installListener?.onFinish(task)
        }
    }
}