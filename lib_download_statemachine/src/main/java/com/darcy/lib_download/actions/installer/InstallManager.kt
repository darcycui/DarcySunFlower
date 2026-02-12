package com.darcy.lib_download.actions.installer

import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.message.lib_common.exts.logE
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object InstallManager {
    private val TAG = InstallManager::class.simpleName
    private val installingMap = mutableMapOf<Int, AppInstallTask>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logE("$TAG:异常:$throwable")
    }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)

    fun startInstall(task: AppInstallTask) {
        synchronized(this) {
            if (installingMap.containsKey(task.itemBean.id)) {
                logE("${task.itemBean.id} 安装任务已存在, 无需重复安装")
                return
            }
            scope.launch {
                installingMap[task.itemBean.id] = task
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
}