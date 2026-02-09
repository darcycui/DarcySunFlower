package com.darcy.lib_download.listener

import com.darcy.lib_download.downloader.DownloadTask
import com.darcy.lib_download.statemachine.IState

interface IStateProgressChangeListener {
    fun onProgressChange(task: DownloadTask, newState: IState, progress: Double)
}