package com.darcy.message.lib_umeng.agent.umeng

import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_umeng.agent.IInitAgent
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

class UMengInitAgentImpl : IInitAgent {
    private val APP_KEY = "670722da667bfe33f3bce631"
    private val CHANNEL = "Huawei"
    private val MASTER_SECRET = "3dpsnfs0xxl51bnxz9pvmkqhvrtkfqop"
    private val MESSAGE_SECRET = "0274cb59706d4c24335d49f6f4e73afc"
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
}