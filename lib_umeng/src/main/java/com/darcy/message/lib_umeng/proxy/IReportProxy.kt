package com.darcy.message.lib_umeng.proxy

import com.darcy.message.lib_umeng.agent.IClickAgent
import com.darcy.message.lib_umeng.agent.IShowAgent
import com.darcy.message.lib_umeng.agent.IUserAgent

interface IReportProxy : IClickAgent, IShowAgent, IUserAgent {
    fun preInit()

    fun init(isDebug: Boolean)
}