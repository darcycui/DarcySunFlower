package com.darcy.lib_download.state

import android.os.Message
import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.lib_download.actions.unziper.UnzipManager
import com.darcy.lib_download.event.AppInstallEvent
import com.darcy.lib_download.listener.IStateProgressChangeListener
import com.darcy.lib_download.statemachine.AppInstallStateMachine
import com.darcy.lib_download.statemachine.State
import com.darcy.lib_download.utils.formatByDigits
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class UnzippingState(
    private val stateMachine: AppInstallStateMachine,
    private var progress: Double = 0.0,
    private val progressChangeListener: IStateProgressChangeListener?
) : State() {
    private val TAG = UnzippingState::class.simpleName
    private var appInstallTask: AppInstallTask? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logE("$TAG:异常:$throwable")
    }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)

    override fun enter() {
        logI("$TAG:进入")
        super.enter()
        scope.launch {
            UnzipManager.startUnzip(stateMachine.getAppInstallTask())
        }
    }

    override fun exit() {
        logE("$TAG:退出")
        super.exit()
    }

    override fun processMessage(msg: Message?): Boolean {
        var processed = false
        when (msg?.obj) {
            is AppInstallEvent.UpdateProgressUnzip -> {
                val newProgress = (msg.obj as AppInstallEvent.UpdateProgressUnzip).progress
                progress = newProgress
                logD("$TAG:解压进度更新:$newProgress")
                progressChangeListener?.onProgressChange(stateMachine.getAppInstallTask(), this, newProgress)
                processed = true
            }

            is AppInstallEvent.FinishUnzipSuccess -> {
                logD("$TAG:解压完成")
                stateMachine.transitionToByClass(UnzipSuccessState::class)
                processed = true
            }

            is AppInstallEvent.FinishUnzipError -> {
                logD("$TAG:解压失败")
                stateMachine.transitionToByClass(UnzipErrorState::class)
                processed = true
            }

            is AppInstallEvent.Reset -> {
                stateMachine.transitionToByClass(InitState::class)
                processed = true
            }
        }
        return processed and super.processMessage(msg)
    }

    override fun getName(): String {
        return super.getName() + ":${(progress * 100).formatByDigits()}%"
    }

    fun getProgress(): Double {
        return progress
    }
}