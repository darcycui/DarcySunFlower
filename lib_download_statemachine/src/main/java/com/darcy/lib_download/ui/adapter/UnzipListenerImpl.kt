package com.darcy.lib_download.ui.adapter

import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.lib_download.actions.unziper.IUnzipListener
import com.darcy.lib_download.event.AppInstallEvent
import com.darcy.lib_download.event.toMessage

/**
 * 解压监听实现类
 */
class UnzipListenerImpl: IUnzipListener {
    override fun onStart(task: AppInstallTask) {
    }

    override fun onProgress(
        task: AppInstallTask,
        progress: Double
    ) {
        val stateMachine = task.itemBean.stateMachine
        stateMachine?.sendMessage(AppInstallEvent.UpdateProgressUnzip(progress).toMessage())
    }

    override fun onCancel(task: AppInstallTask) {
    }

    override fun onFinish(task: AppInstallTask) {
        val stateMachine = task.itemBean.stateMachine
        stateMachine?.sendMessage(AppInstallEvent.FinishUnzipSuccess.toMessage())
    }

    override fun onError(
        task: AppInstallTask,
        e: Exception
    ) {
        val stateMachine = task.itemBean.stateMachine
        stateMachine?.sendMessage(AppInstallEvent.FinishUnzipError(e).toMessage())
    }
}
