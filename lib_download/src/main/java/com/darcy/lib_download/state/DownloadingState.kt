package com.darcy.lib_download.state

import android.os.Message
import com.darcy.lib_download.event.DownloadEvent
import com.darcy.lib_download.statemachine.DownloadStateMachine
import com.darcy.lib_download.statemachine.State
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI

class DownloadingState : State() {
    val TAG = DownloadingState::class.simpleName

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
                DownloadStateMachine.getInstance().transitionToByClass(PauseState::class)
                processed = true
            }
        }
        return processed and super.processMessage(msg)
    }
}