package com.darcy.lib_download.bean

import com.darcy.lib_download.statemachine.DownloadStateMachine

data class ItemBean(
    val id: Int,
    val name: String,
    val url: String,
    val size: Long,
    var stateMachine: DownloadStateMachine? = null
)
