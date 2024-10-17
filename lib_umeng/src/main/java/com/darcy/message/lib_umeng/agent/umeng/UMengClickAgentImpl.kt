package com.darcy.message.lib_umeng.agent.umeng

import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_umeng.agent.IClickAgent
import com.umeng.analytics.MobclickAgent

class UMengClickAgentImpl : IClickAgent {
    override fun clickEvent(eventName: String, params: Map<String, Any?>?) {
        if (params.isNullOrEmpty()) {
            MobclickAgent.onEvent(AppHelper.getAppContext(), eventName)
        } else {
            MobclickAgent.onEventObject(AppHelper.getAppContext(), eventName, params)
        }
    }
}