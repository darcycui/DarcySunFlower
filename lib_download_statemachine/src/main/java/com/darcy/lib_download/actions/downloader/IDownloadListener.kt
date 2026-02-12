package com.darcy.lib_download.actions.downloader

import com.darcy.lib_download.actions.AppInstallTask

interface IDownloadListener {
    fun onStart(task: AppInstallTask)
    fun onProgress(task: AppInstallTask, progress: Double)
    fun onPause(task: AppInstallTask)
    fun onResume(task: AppInstallTask)
    fun onCancel(task: AppInstallTask)
    fun onFinish(task: AppInstallTask)
    fun onError(task: AppInstallTask, e: Exception)
}