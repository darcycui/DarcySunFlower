package com.darcy.lib_download.listener

import com.darcy.lib_download.statemachine.IState

interface IStateProgressChangeListener {
    fun onProgressChange(newState: IState, progress: Double)
}