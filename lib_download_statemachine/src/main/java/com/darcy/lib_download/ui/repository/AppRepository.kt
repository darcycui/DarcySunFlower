package com.darcy.lib_download.ui.repository

import com.darcy.lib_download.bean.ItemBean
import com.darcy.lib_download.state.DownloadPauseState
import com.darcy.lib_download.state.InitState
import com.darcy.lib_download.statemachine.AppInstallStateMachine

class AppRepository {

    suspend fun getAppList(): List<ItemBean> {
        val list = listOf(
            ItemBean(
                id = 1,
                name = "App1",
                url = "https://www.baidu.com",
                size = 1000L,
                lastStateClass = InitState::class,
                stateMachine = AppInstallStateMachine.empty(),
                downloadingProgress = 0.0,
                unzipProgress = 0.0,
                installProgress = 0.0,
                isPaused = false
            ),
            ItemBean(
                id = 2,
                name = "App2",
                url = "https://www.sina.com",
                size = 2000L,
                lastStateClass = DownloadPauseState::class,
                stateMachine = AppInstallStateMachine.empty(),
                downloadingProgress = 0.20,
                unzipProgress = 0.0,
                installProgress = 0.0,
                isPaused = true
            ),
        )
        return list
    }
}