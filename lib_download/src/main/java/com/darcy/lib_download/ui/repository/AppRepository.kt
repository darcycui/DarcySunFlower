package com.darcy.lib_download.ui.repository

import com.darcy.lib_download.bean.ItemBean

class AppRepository {

    suspend fun getAppList(): List<ItemBean> {
        val list = listOf(
            ItemBean(
                id = 1,
                name = "App1",
                url = "https://www.baidu.com",
                size = 1000L,
            ),
            ItemBean(
                id = 2,
                name = "App2",
                url = "https://www.sina.com",
                size = 2000L,
            ),
        )
        return list
    }
}