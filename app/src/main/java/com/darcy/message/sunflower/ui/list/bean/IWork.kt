package com.darcy.message.sunflower.ui.list.bean

import com.darcy.message.lib_common.exts.logI
import javax.inject.Inject

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