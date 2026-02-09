package com.darcy.lib_download.statemachine

import android.os.Message
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
    private val initState = InitState()
    private val downloadingState = DownloadingState(progressCallback)
    private val pauseState = PauseState()

    companion object {
        @Volatile
        private var instance: DownloadStateMachine? = null
        fun init(
            stateCallback: IStateChangeListener,
            progressCallback: IStateProgressChangeListener?
        ) {
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

        fun start() {
            instance?.start()
                ?: throw NullPointerException("DownloadStateMachine is null. Call init first.")
        }

        fun stop(quitNow: Boolean = true) {
            instance?.apply {
                if (quitNow) {
                    quitNow()
                } else {
                    quit()
                }
                instance = null
            }
        }
    }

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

    fun transitionToByClass(stateClass: KClass<*>) {
        when (stateClass) {
            InitState::class -> transitionTo(initState)
            DownloadingState::class -> transitionTo(downloadingState)
            PauseState::class -> transitionTo(pauseState)
        }
    }

    override fun onPreHandleMessage(msg: Message?) {
        super.onPreHandleMessage(msg)
        stateCallback.onStatePreChange(currentState, msg)
    }

    override fun onPostHandleMessage(msg: Message?) {
        super.onPostHandleMessage(msg)
        stateCallback.onStateChanged(currentState, msg)
    }
}