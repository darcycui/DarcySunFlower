package com.darcy.lib_download.statemachine

import android.os.Message
import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.lib_download.listener.IStateChangeListener
import com.darcy.lib_download.listener.IStateProgressChangeListener
import com.darcy.lib_download.state.DownloadErrorState
import com.darcy.lib_download.state.DownloadPauseState
import com.darcy.lib_download.state.DownloadSuccessState
import com.darcy.lib_download.state.DownloadingState
import com.darcy.lib_download.state.InitState
import com.darcy.lib_download.state.InstallErrorState
import com.darcy.lib_download.state.InstallSuccessState
import com.darcy.lib_download.state.InstallingState
import com.darcy.lib_download.state.UnzipErrorState
import com.darcy.lib_download.state.UnzipSuccessState
import com.darcy.lib_download.state.UnzippingState
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import kotlin.reflect.KClass

/**
 * 需要存储的值：
 * 当前状态
 * 当前进度
 */
class AppInstallStateMachine(
    private val firstStateClass: KClass<out State>?,
    private val stateCallback: IStateChangeListener?,
    private val progressCallback: IStateProgressChangeListener?,
) : StateMachine("DownloadStateMachine") {
    private val initState = InitState(this)
    private val downloadingState = DownloadingState(this, 0.0, progressCallback)
    private val downloadPauseState = DownloadPauseState(this)
    private val downloadSuccessState = DownloadSuccessState(this)
    private val downloadErrorState = DownloadErrorState(this)

    private val unzippingState = UnzippingState(this, 0.0, progressCallback)
    private val unzipSuccessState = UnzipSuccessState(this)
    private val unzipErrorState = UnzipErrorState(this)

    private val installingState = InstallingState(this, 0.0, progressCallback)
    private val installSuccessState = InstallSuccessState(this)
    private val installErrorState = InstallErrorState(this)

    companion object {
        @Volatile
        private var instance: AppInstallStateMachine? = null

        fun empty(): AppInstallStateMachine {
            return AppInstallStateMachine(null, null, null)
        }

        fun init(
            stateCallback: IStateChangeListener,
            progressCallback: IStateProgressChangeListener?,
            firstState: KClass<out State>,
        ) {
            if (instance == null) {
                synchronized(AppInstallStateMachine::class.java) {
                    if (instance == null) {
                        instance =
                            AppInstallStateMachine(firstState, stateCallback, progressCallback)
                    }
                }
            }
        }

        fun getInstance(): AppInstallStateMachine {
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

    private var appInstallTask: AppInstallTask? = null

    init {
        logI("状态机初始化 init")
        // 添加状态
        // 初始状态
        addState(initState)
        // 下载状态
        addState(downloadingState)
        addState(downloadPauseState)
        addState(downloadSuccessState)
        addState(downloadErrorState)
        // 解压状态
        addState(unzippingState)
        addState(unzipSuccessState)
        addState(unzipErrorState)
        // 安装状态
        addState(installingState)
        addState(installSuccessState)
        addState(installErrorState)

        // 设置初始状态
        val firstState = when (firstStateClass) {
            DownloadingState::class -> downloadingState
            DownloadPauseState::class -> downloadPauseState
            DownloadSuccessState::class -> downloadSuccessState
            DownloadErrorState::class -> downloadErrorState
            UnzippingState::class -> unzippingState
            UnzipSuccessState::class -> unzipSuccessState
            UnzipErrorState::class -> unzipErrorState
            InstallingState::class -> installingState
            InstallSuccessState::class -> installSuccessState
            InstallErrorState::class -> installErrorState
            else -> {
                initState
            }
        }
        setInitialState(firstState)
        // 启动状态机
        start()
    }

    fun transitionToByClass(stateClass: KClass<*>) {
        when (stateClass) {
            InitState::class -> transitionTo(initState)
            DownloadingState::class -> transitionTo(downloadingState)
            DownloadPauseState::class -> transitionTo(downloadPauseState)
            DownloadSuccessState::class -> transitionTo(downloadSuccessState)
            DownloadErrorState::class -> transitionTo(downloadErrorState)
            UnzippingState::class -> transitionTo(unzippingState)
            UnzipSuccessState::class -> transitionTo(unzipSuccessState)
            UnzipErrorState::class -> transitionTo(unzipErrorState)
            InstallingState::class -> transitionTo(installingState)
            InstallSuccessState::class -> transitionTo(installSuccessState)
            InstallErrorState::class -> transitionTo(installErrorState)
            else -> throw IllegalArgumentException("Invalid state class: $stateClass")
        }
    }

    fun setupAppInstallTask(task: AppInstallTask) {
        this.appInstallTask = task
    }

    fun getAppInstallTask(): AppInstallTask {
        return appInstallTask
            ?: throw NullPointerException("appInstallTask is null. Call setupAppInstallTask() first.")
    }

    override fun onPreHandleMessage(msg: Message?) {
        super.onPreHandleMessage(msg)
        if (appInstallTask == null) {
            throw NullPointerException("appInstallTask is null. Call setupAppInstallTask() first.")
        }
        stateCallback?.onStatePreChange(getAppInstallTask(), currentState, msg)
    }

    override fun onPostHandleMessage(msg: Message?) {
        super.onPostHandleMessage(msg)
        stateCallback?.onStateChanged(getAppInstallTask(), currentState, msg)
    }

    override fun onQuitting() {
        super.onQuitting()
        logE("状态机退出 onQuitting")
    }
}