package com.darcy.lib_download.listener

import android.os.Message
import com.darcy.lib_download.downloader.DownloadTask
import com.darcy.lib_download.statemachine.IState

interface IStateChangeListener {
    fun onStateChanged(task: DownloadTask, currentState: IState, message: Message?)

    fun onStatePreChange(task: DownloadTask, currentState: IState, message: Message?)
}