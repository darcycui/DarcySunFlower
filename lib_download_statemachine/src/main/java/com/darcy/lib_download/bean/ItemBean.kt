package com.darcy.lib_download.bean

import com.darcy.lib_download.statemachine.AppInstallStateMachine
import com.darcy.lib_download.statemachine.State
import kotlin.reflect.KClass

data class ItemBean(
    val id: Int,
    val name: String,
    val url: String,
    val size: Long,
    var stateMachine: AppInstallStateMachine,
    val lastStateClass: KClass<out State>,
    var downloadingProgress: Double,
    var unzipProgress: Double,
    var installProgress: Double,
    var isPaused: Boolean,
)
