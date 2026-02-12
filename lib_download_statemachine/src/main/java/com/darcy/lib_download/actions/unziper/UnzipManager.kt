package com.darcy.lib_download.actions.unziper

import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.message.lib_common.exts.logE
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object UnzipManager {
    private val TAG = UnzipManager::class.simpleName
    private val unzippingMap = mutableMapOf<Int, AppInstallTask>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logE("$TAG:异常:$throwable")
    }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)

    private val mutex: Mutex = Mutex()

    fun startUnzip(task: AppInstallTask) {
        scope.launch {
            mutex.withLock {
                if (unzippingMap.containsKey(task.itemBean.id)) {
                    logE("${task.itemBean.id} 解压任务已存在, 无需重复解压")
                    return@withLock
                }
                unzippingMap[task.itemBean.id] = task
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
}