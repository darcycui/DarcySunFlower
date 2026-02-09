package com.darcy.lib_download.statemachine

import android.os.Message
import com.darcy.lib_download.downloader.DownloadTask
import com.darcy.lib_download.listener.IStateChangeListener
import com.darcy.lib_download.listener.IStateProgressChangeListener
import com.darcy.lib_download.state.DownloadingState
import com.darcy.lib_download.state.FinishErrorState
import com.darcy.lib_download.state.FinishSuccessState
import com.darcy.lib_download.state.InitState
import com.darcy.lib_download.state.PauseState
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import kotlin.reflect.KClass

/**
 * 需要存储的值：
 * 当前状态
 * 当前进度
 */
class DownloadStateMachine(
    private val stateCallback: IStateChangeListener,
    private val progressCallback: IStateProgressChangeListener?
) : StateMachine("DownloadStateMachine") {
    private val initState = InitState(this)
    private val downloadingState = DownloadingState(this, 0.0, progressCallback)
    private val pauseState = PauseState(this)
    private val finishSuccessState = FinishSuccessState(this)
    private val finishErrorState = FinishErrorState(this)

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

    private var downloadTask: DownloadTask? = null

    init {
        logI("状态机初始化 init")
        // 添加状态
        addState(initState)
        addState(downloadingState)
        addState(pauseState)
        addState(finishSuccessState)
        addState(finishErrorState)

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
            FinishSuccessState::class -> transitionTo(finishSuccessState)
            FinishErrorState::class -> transitionTo(finishErrorState)
            else -> throw IllegalArgumentException("Invalid state class: $stateClass")
        }
    }

    fun setupDownloadTask(task: DownloadTask) {
        this.downloadTask = task
        downloadingState.setupDownloadTask(task)
    }

    override fun onPreHandleMessage(msg: Message?) {
        super.onPreHandleMessage(msg)
        stateCallback.onStatePreChange(downloadTask!!, currentState, msg)
    }

    override fun onPostHandleMessage(msg: Message?) {
        super.onPostHandleMessage(msg)
        stateCallback.onStateChanged(downloadTask!!, currentState, msg)
    }

    override fun onQuitting() {
        super.onQuitting()
        logE("状态机退出 onQuitting")
    }
}