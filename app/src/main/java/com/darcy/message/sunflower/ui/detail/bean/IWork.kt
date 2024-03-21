package com.darcy.message.sunflower.ui.detail.bean

import com.darcy.message.lib_common.exts.logI
import javax.inject.Inject
import javax.inject.Qualifier

interface IWork {
    fun work()
}

class WorkA @Inject constructor() : IWork {
    override fun work() {
        logI(message = "workA")
    }
}

class WorkB @Inject constructor() : IWork {
    override fun work() {
        logI(message = "workB")
    }
}