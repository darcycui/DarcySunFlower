package com.darcy.lib_download.state

import android.os.Message
import com.darcy.lib_download.actions.downloader.DownloadManager
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

class DownloadingState(
    private val stateMachine: AppInstallStateMachine,
    private var progress: Double = 0.0,
    private val progressChangeListener: IStateProgressChangeListener?
) : State() {
    private val TAG = DownloadingState::class.simpleName
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logE("$TAG:异常:$throwable")
    }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)


    override fun enter() {
        logI("$TAG:进入")
        super.enter()
        scope.launch {
            DownloadManager.startDownload(stateMachine.getAppInstallTask())
        }
    }

    override fun exit() {
        logE("$TAG:退出")
        super.exit()
    }

    override fun processMessage(msg: Message?): Boolean {
        var processed = false
        when (msg?.obj) {
            AppInstallEvent.PauseDownload -> {
                logD("$TAG:暂停下载")
                stateMachine.transitionToByClass(DownloadPauseState::class)
                processed = true
            }

            is AppInstallEvent.UpdateProgressDownload -> {
                val newProgress = (msg.obj as AppInstallEvent.UpdateProgressDownload).progress
                progress = newProgress
                logD("$TAG:下载进度更新:$newProgress")
                progressChangeListener?.onProgressChange(
                    stateMachine.getAppInstallTask(),
                    this,
                    newProgress
                )
                processed = true
            }

            is AppInstallEvent.FinishDownloadSuccess -> {
                logD("$TAG:下载完成")
                stateMachine.transitionToByClass(DownloadSuccessState::class)
                processed = true
            }

            is AppInstallEvent.FinishDownloadError -> {
                logD("$TAG:下载失败")
                stateMachine.transitionToByClass(DownloadErrorState::class)
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