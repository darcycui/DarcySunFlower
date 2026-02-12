package com.darcy.lib_download.listener

import android.os.Message
import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.lib_download.statemachine.IState

interface IStateChangeListener {
    fun onStateChanged(task: AppInstallTask, currentState: IState, message: Message?)

    fun onStatePreChange(task: AppInstallTask, currentState: IState, message: Message?)
}