package com.darcy.message.lib_umeng.agent.umeng

import com.darcy.message.lib_umeng.agent.ICrashAgent
import com.umeng.commonsdk.internal.crash.UMCrashManager

class UMengCrashAgent :ICrashAgent{
    override fun registerCrashCallback() {

    }

    override fun unregisterCrashCallback() {
        TODO("Not yet implemented")
    }

    override fun reportCustomCrash(throwable: Throwable) {
        TODO("Not yet implemented")
    }
}