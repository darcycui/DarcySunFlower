package com.darcy.lib_download.listener

import android.os.Message
import com.darcy.lib_download.statemachine.IState

interface IStateChangeListener {
    fun onStateChanged(currentState: IState, message: Message?)

    fun onStatePreChange(currentState: IState, message: Message?)
}