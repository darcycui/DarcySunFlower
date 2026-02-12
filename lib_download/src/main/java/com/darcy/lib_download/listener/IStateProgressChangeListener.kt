package com.darcy.lib_download.listener

import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.lib_download.statemachine.IState

interface IStateProgressChangeListener {
    fun onProgressChange(task: AppInstallTask, newState: IState, progress: Double)
}