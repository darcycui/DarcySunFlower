package com.darcy.lib_download.listener

import com.darcy.lib_download.statemachine.IState

interface IStateMachineListener {
    fun onStateChange(newState: IState)
}