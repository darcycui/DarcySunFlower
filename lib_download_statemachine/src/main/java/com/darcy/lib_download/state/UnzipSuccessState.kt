package com.darcy.lib_download.state

import android.os.Message
import com.darcy.lib_download.event.AppInstallEvent
import com.darcy.lib_download.event.toMessage
import com.darcy.lib_download.statemachine.AppInstallStateMachine
import com.darcy.lib_download.statemachine.State
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI

class UnzipSuccessState(
    private val stateMachine: AppInstallStateMachine,
) : State() {
    private val TAG = UnzipSuccessState::class.simpleName

    override fun enter() {
        logI("$TAG:进入")
        super.enter()
        stateMachine.sendMessage(AppInstallEvent.StartInstall.toMessage())
    }

    override fun exit() {
        logE("$TAG:退出")
        super.exit()
    }

    override fun processMessage(msg: Message?): Boolean {
        var processed = false
        when (msg?.obj) {
            is AppInstallEvent.StartInstall -> {
                stateMachine.transitionToByClass(InstallingState::class)
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