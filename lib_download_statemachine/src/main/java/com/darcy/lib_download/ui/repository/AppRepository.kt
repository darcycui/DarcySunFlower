package com.darcy.lib_download.ui.repository

import com.darcy.lib_download.bean.ItemBean
import com.darcy.lib_download.state.DownloadPauseState
import com.darcy.lib_download.state.InitState

class AppRepository {

    suspend fun getAppList(): List<ItemBean> {
        val list = listOf(
            ItemBean(
                id = 1,
                name = "App1",
                url = "https://www.baidu.com",
                size = 1000L,
                lastStateClass = InitState::class,
                downloadingProgress = 0.0,
                unzipProgress = 0.0,
                installProgress = 0.0
            ),
            ItemBean(
                id = 2,
                name = "App2",
                url = "https://www.sina.com",
                size = 2000L,
                lastStateClass = DownloadPauseState::class,
                downloadingProgress = 0.20,
                unzipProgress = 0.0,
                installProgress = 0.0
            ),
        )
        return list
    }
}