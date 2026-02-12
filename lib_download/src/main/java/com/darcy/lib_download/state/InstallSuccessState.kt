package com.darcy.lib_download.state

import android.os.Message
import com.darcy.lib_download.event.AppInstallEvent
import com.darcy.lib_download.statemachine.AppInstallStateMachine
import com.darcy.lib_download.statemachine.State
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI

class InstallSuccessState(
    private val stateMachine: AppInstallStateMachine
) : State() {
    private val TAG = InstallSuccessState::class.simpleName

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

            is AppInstallEvent.Reset -> {
                stateMachine.transitionToByClass(InitState::class)
                processed = true
            }
        }
        return processed and super.processMessage(msg)
    }
}