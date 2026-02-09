package com.darcy.lib_download.state

import android.os.Message
import com.darcy.lib_download.downloader.DownloadTask
import com.darcy.lib_download.event.DownloadEvent
import com.darcy.lib_download.listener.IStateProgressChangeListener
import com.darcy.lib_download.statemachine.DownloadStateMachine
import com.darcy.lib_download.statemachine.State
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI

class DownloadingState(
    private val stateMachine: DownloadStateMachine,
    private var progress: Double = 0.0,
    private val progressChangeListener: IStateProgressChangeListener?
) : State() {
    private val TAG = DownloadingState::class.simpleName
    private var downloadTask: DownloadTask? = null

    override fun enter() {
        logI("$TAG:进入")
        super.enter()
    }

    override fun exit() {
        logE("$TAG:退出")
        super.exit()
    }

    override fun processMessage(msg: Message?): Boolean {
        var processed = false
        when (msg?.obj) {
            DownloadEvent.Pause -> {
                logD("$TAG:暂停下载")
                stateMachine.transitionToByClass(PauseState::class)
                processed = true
            }

            is DownloadEvent.ProgressUpdate -> {
                val newProgress = (msg.obj as DownloadEvent.ProgressUpdate).progress
                progress = newProgress
                logD("$TAG:下载进度更新:$newProgress")
                progressChangeListener?.onProgressChange(downloadTask!!, this, newProgress)
                processed = true
            }

            is DownloadEvent.FinishSuccess -> {
                logD("$TAG:下载完成")
                stateMachine.transitionToByClass(FinishSuccessState::class)
                processed = true
            }

            is DownloadEvent.FinishError -> {
                logD("$TAG:下载失败")
                stateMachine.transitionToByClass(FinishErrorState::class)
                processed = true
            }
        }
        return processed and super.processMessage(msg)
    }

    override fun getName(): String {
        return super.getName() + ":$progress"
    }

    fun getProgress(): Double {
        return progress
    }

    fun setupDownloadTask(downloadTask: DownloadTask) {
        this.downloadTask = downloadTask
    }
}