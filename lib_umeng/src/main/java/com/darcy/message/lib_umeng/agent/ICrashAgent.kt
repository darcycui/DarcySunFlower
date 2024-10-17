package com.darcy.message.lib_umeng.agent

interface ICrashAgent {
    fun registerCrashCallback()

    fun unregisterCrashCallback()

    fun reportCustomCrash(throwable: Throwable)

}