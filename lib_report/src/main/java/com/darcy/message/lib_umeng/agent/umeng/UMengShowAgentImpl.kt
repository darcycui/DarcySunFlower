package com.darcy.message.lib_umeng.agent.umeng

import android.content.Context
import com.darcy.message.lib_umeng.agent.IShowAgent
import com.umeng.analytics.MobclickAgent

class UMengShowAgentImpl : IShowAgent {
    override fun pageStart(context: Context, pageName: String, params: Map<String, Any?>?) {
        MobclickAgent.onPageStart(pageName)
    }

    override fun pageEnd(context: Context,pageName: String, params: Map<String, Any?>?) {
        MobclickAgent.onPageEnd(pageName)
    }
}