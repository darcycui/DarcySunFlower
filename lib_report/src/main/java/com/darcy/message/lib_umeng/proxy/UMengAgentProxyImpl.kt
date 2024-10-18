package com.darcy.message.lib_umeng.proxy

import android.content.Context
import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_umeng.agent.IClickAgent
import com.darcy.message.lib_umeng.agent.IInitAgent
import com.darcy.message.lib_umeng.agent.IShowAgent
import com.darcy.message.lib_umeng.agent.IUserAgent
import com.darcy.message.lib_umeng.agent.umeng.UMengClickAgentImpl
import com.darcy.message.lib_umeng.agent.umeng.UMengInitAgentImpl
import com.darcy.message.lib_umeng.agent.umeng.UMengShowAgentImpl
import com.darcy.message.lib_umeng.agent.umeng.UmengUserAgentImpl
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

object UMengAgentProxyImpl : IAgentProxy {

    private val iInitAgent: IInitAgent = UMengInitAgentImpl()
    private val iUserAgent: IUserAgent = UmengUserAgentImpl()
    private val iClickAgent: IClickAgent = UMengClickAgentImpl()
    private val iShowAgent: IShowAgent = UMengShowAgentImpl()

    override fun preInit() {
        iInitAgent.preInit()
    }

    override fun init(isDebug: Boolean) {
        iInitAgent.init(isDebug)
    }

    override fun clickEvent(context: Context, eventName: String, params: Map<String, Any?>?) {
        iClickAgent.clickEvent(context, eventName, params)
    }

    override fun pageStart(context: Context, pageName: String, params: Map<String, Any?>?) {
        iShowAgent.pageStart(context, pageName, params)
    }

    override fun pageEnd(context: Context, pageName: String, params: Map<String, Any?>?) {
        iShowAgent.pageEnd(context, pageName, params)
    }

    override fun signIn(userID: String) {
        iUserAgent.signIn(userID)
    }

    override fun signIn(provider: String, userID: String) {
        iUserAgent.signIn(provider, userID)
    }

    override fun signOut(userID: String) {
        iUserAgent.signOut(userID)
    }

    override fun signOut(provider: String, userID: String) {
        iUserAgent.signOut(provider, userID)
    }

    override fun onResume(context: Context, pageName: String, params: Map<String, Any?>?) {
        MobclickAgent.onResume(context)
    }

    override fun onPause(context: Context, pageName: String, params: Map<String, Any?>?) {
        MobclickAgent.onPause(context)
    }
}