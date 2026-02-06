package com.darcy.lib_download.listener

import com.darcy.lib_download.statemachine.IState

interface IStateChangeListener {
    fun onStateChange(newState: IState)
}