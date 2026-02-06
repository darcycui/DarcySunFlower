package com.darcy.lib_download.statemachine

import com.darcy.lib_download.listener.IStateChangeListener
import com.darcy.lib_download.listener.IStateProgressChangeListener
import com.darcy.lib_download.state.DownloadingState
import com.darcy.lib_download.state.InitState
import com.darcy.lib_download.state.PauseState
import kotlin.reflect.KClass

class DownloadStateMachine(
    private val stateCallback: IStateChangeListener,
    private val progressCallback: IStateProgressChangeListener?
) : StateMachine("DownloadStateMachine") {
    private val initState = InitState(stateCallback)
    private val downloadingState = DownloadingState(stateCallback, progressCallback)
    private val pauseState = PauseState(stateCallback)

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
        fun init(stateCallback: IStateChangeListener, progressCallback: IStateProgressChangeListener?) {
            if (instance == null) {
                synchronized(DownloadStateMachine::class.java) {
                    if (instance == null) {
                        instance = DownloadStateMachine(stateCallback, progressCallback)
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