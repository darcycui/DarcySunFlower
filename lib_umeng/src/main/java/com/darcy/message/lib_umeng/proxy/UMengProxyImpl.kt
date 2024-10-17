package com.darcy.message.lib_umeng.proxy

import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_umeng.agent.IClickAgent
import com.darcy.message.lib_umeng.agent.IShowAgent
import com.darcy.message.lib_umeng.agent.IUserAgent
import com.darcy.message.lib_umeng.agent.umeng.UMengClickAgentImpl
import com.darcy.message.lib_umeng.agent.umeng.UMengShowAgentImpl
import com.darcy.message.lib_umeng.agent.umeng.UmengUserAgentImpl
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

object UMengProxyImpl : IReportProxy {
    private const val APP_KEY = "670722da667bfe33f3bce631"
    private const val CHANNEL = "Huawei"
    private const val MASTER_SECRET = "3dpsnfs0xxl51bnxz9pvmkqhvrtkfqop"
    private const val MESSAGE_SECRET = "0274cb59706d4c24335d49f6f4e73afc"
    private val iUserAgent: IUserAgent = UmengUserAgentImpl()
    private val iClickAgent: IClickAgent = UMengClickAgentImpl()
    private val iShowAgent: IShowAgent = UMengShowAgentImpl()
    override fun preInit() {
        UMConfigure.preInit(AppHelper.getAppContext(), APP_KEY, CHANNEL)
    }

    override fun init(isDebug: Boolean) {
        UMConfigure.init(
            AppHelper.getAppContext(),
            APP_KEY,
            CHANNEL,
            UMConfigure.DEVICE_TYPE_PHONE,
            MESSAGE_SECRET
        )
        UMConfigure.setLogEnabled(isDebug)
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
//        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL)
        logE("init umeng success: showLog=$isDebug")
    }

    override fun clickEvent(eventName: String, params: Map<String, Any?>?) {
        iClickAgent.clickEvent(eventName, params)
    }

    override fun pageStart(pageName: String, params: Map<String, Any?>?) {
        iShowAgent.pageStart(pageName, params)
    }

    override fun pageEnd(pageName: String, params: Map<String, Any?>?) {
        iShowAgent.pageEnd(pageName, params)
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
}