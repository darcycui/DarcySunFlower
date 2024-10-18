package com.darcy.message.lib_umeng.proxy

import com.darcy.message.lib_umeng.agent.IClickAgent
import com.darcy.message.lib_umeng.agent.IDurationAgent
import com.darcy.message.lib_umeng.agent.IInitAgent
import com.darcy.message.lib_umeng.agent.IShowAgent
import com.darcy.message.lib_umeng.agent.IUserAgent

interface IAgentProxy : IClickAgent, IShowAgent, IUserAgent, IDurationAgent, IInitAgent {

}