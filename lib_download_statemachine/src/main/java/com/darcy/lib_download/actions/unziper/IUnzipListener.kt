package com.darcy.lib_download.actions.unziper

import com.darcy.lib_download.actions.AppInstallTask

interface IUnzipListener {
    fun onStart(task: AppInstallTask)
    fun onProgress(task: AppInstallTask, progress: Double)
    fun onCancel(task: AppInstallTask)
    fun onFinish(task: AppInstallTask)
    fun onError(task: AppInstallTask, e: Exception)
}