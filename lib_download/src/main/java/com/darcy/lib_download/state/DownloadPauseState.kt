package com.darcy.lib_download.state

import android.os.Message
import com.darcy.lib_download.event.AppInstallEvent
import com.darcy.lib_download.statemachine.AppInstallStateMachine
import com.darcy.lib_download.statemachine.State
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI

class DownloadPauseState(
    private val stateMachine: AppInstallStateMachine
) : State() {
    private val TAG = DownloadPauseState::class.simpleName

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
            AppInstallEvent.ResumeDownload -> {
                logD("$TAG:恢复下载")
                stateMachine.transitionToByClass(DownloadingState::class)
                processed = true
            }

            is AppInstallEvent.Reset -> {
                stateMachine.transitionToByClass(InitState::class)
                processed = true
            }
        }
        return processed and super.processMessage(msg)
    }
}