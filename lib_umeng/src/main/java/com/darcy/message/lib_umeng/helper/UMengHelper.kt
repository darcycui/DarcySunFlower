package com.darcy.message.lib_umeng.helper

import com.darcy.message.lib_common.app.AppHelper
import com.umeng.commonsdk.UMConfigure

object UMengHelper {
    private const val APP_KEY = "670722da667bfe33f3bce631"
    private const val CHANNEL = "Huawei"
    private const val MASTER_SECRET = "3dpsnfs0xxl51bnxz9pvmkqhvrtkfqop"
    private const val MESSAGE_SECRET = "0274cb59706d4c24335d49f6f4e73afc"
    fun preInit() {
        UMConfigure.preInit(AppHelper.getAppContext(), APP_KEY, CHANNEL)
    }

    fun init(isDebug: Boolean) {
        UMConfigure.init(AppHelper.getAppContext(), APP_KEY, CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, MESSAGE_SECRET)
        UMConfigure.setLogEnabled(isDebug)
    }
}