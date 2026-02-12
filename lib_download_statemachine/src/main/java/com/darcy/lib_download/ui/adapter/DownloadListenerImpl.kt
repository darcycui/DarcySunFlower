package com.darcy.lib_download.ui.adapter

import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.lib_download.actions.downloader.IDownloadListener
import com.darcy.lib_download.event.AppInstallEvent
import com.darcy.lib_download.event.toMessage
import com.darcy.message.lib_common.exts.logV

/**
 * 下载监听实现类
 */
class DownloadListenerImpl: IDownloadListener {
    override fun onStart(task: AppInstallTask) {
        val stateMachine = task.itemBean.stateMachine
        //stateMachine.sendMessage(DownloadEvent.StartDownload.toMessage())
    }

    override fun onProgress(
        task: AppInstallTask,
        progress: Double
    ) {
        val stateMachine = task.itemBean.stateMachine
        stateMachine.sendMessage(AppInstallEvent.UpdateProgressDownload(progress).toMessage())
    }

    override fun onPause(task: AppInstallTask) {
        val stateMachine = task.itemBean.stateMachine
        stateMachine.sendMessage(AppInstallEvent.PauseDownload.toMessage())
    }

    override fun onResume(task: AppInstallTask) {
        val stateMachine = task.itemBean.stateMachine
        stateMachine.sendMessage(AppInstallEvent.ResumeDownload.toMessage())
    }

    override fun onCancel(task: AppInstallTask) {
    }

    override fun onFinish(task: AppInstallTask) {
        val stateMachine = task.itemBean.stateMachine
        stateMachine.sendMessage(AppInstallEvent.FinishDownloadSuccess.toMessage())
    }

    override fun onError(
        task: AppInstallTask,
        e: Exception
    ) {
        val stateMachine = task.itemBean.stateMachine
        stateMachine.sendMessage(AppInstallEvent.FinishDownloadError(e).toMessage())
    }

}