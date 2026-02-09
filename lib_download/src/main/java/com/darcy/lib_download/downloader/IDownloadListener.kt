package com.darcy.lib_download.downloader

interface IDownloadListener {
    fun onStart(task: DownloadTask)
    fun onProgress(task: DownloadTask, progress: Double)
    fun onPause(task: DownloadTask)
    fun onResume(task: DownloadTask)
    fun onCancel(task: DownloadTask)
    fun onFinish(task: DownloadTask)
    fun onError(task: DownloadTask, e: Exception)
}