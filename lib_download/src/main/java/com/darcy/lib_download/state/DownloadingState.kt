package com.darcy.lib_download.state

import android.os.Message
import com.darcy.lib_download.event.DownloadEvent
import com.darcy.lib_download.listener.IStateChangeListener
import com.darcy.lib_download.listener.IStateProgressChangeListener
import com.darcy.lib_download.statemachine.DownloadStateMachine
import com.darcy.lib_download.statemachine.State
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI

class DownloadingState(
    private val callback: IStateChangeListener?,
    private val progressChangeListener: IStateProgressChangeListener?
) : State() {
    private val TAG = DownloadingState::class.simpleName
    private var progress: Double = 0.0

    override fun enter() {
        logI("$TAG:进入")
        super.enter()
        callback?.onStateChange(this)
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
                DownloadStateMachine.getInstance().transitionToByClass(PauseState::class)
                processed = true
            }

            is DownloadEvent.ProgressUpdate -> {
                val newProgress = (msg.obj as DownloadEvent.ProgressUpdate).progress
                progress = newProgress
                logD("$TAG:下载进度更新:$newProgress")
                progressChangeListener?.onProgressChange(this, newProgress)
                processed = true
            }
        }
        return processed and super.processMessage(msg)
    }

    override fun getName(): String {
        return super.getName() + ":$progress"
    }
}