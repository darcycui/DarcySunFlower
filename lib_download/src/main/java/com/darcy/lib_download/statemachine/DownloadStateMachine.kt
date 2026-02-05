package com.darcy.lib_download.statemachine

import com.darcy.lib_download.listener.IStateMachineListener
import com.darcy.lib_download.state.DownloadingState
import com.darcy.lib_download.state.InitState
import com.darcy.lib_download.state.PauseState
import kotlin.reflect.KClass

class DownloadStateMachine(
    private val callback: IStateMachineListener
) : StateMachine("DownloadStateMachine") {
    private val initState = InitState(callback)
    private val downloadingState = DownloadingState(callback)
    private val pauseState = PauseState(callback)

    init {
        // 添加状态
        addState(initState)
        addState(downloadingState)
        addState(pauseState)

        // 设置初始状态
        setInitialState(initState)
        // 启动状态机
        start()
    }

    companion object {
        @Volatile
        private var instance: DownloadStateMachine? = null
        fun init(callback: IStateMachineListener) {
            if (instance == null) {
                synchronized(DownloadStateMachine::class.java) {
                    if (instance == null) {
                        instance = DownloadStateMachine(callback)
                    }
                }
            }
        }

        fun getInstance(): DownloadStateMachine {
            return instance!!
        }
    }

    fun transitionToByClass(stateClass: KClass<*>) {
        when (stateClass) {
            InitState::class -> transitionTo(initState)
            DownloadingState::class -> transitionTo(downloadingState)
            PauseState::class -> transitionTo(pauseState)
        }
    }
}