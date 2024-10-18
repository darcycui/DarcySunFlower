package com.darcy.message.lib_umeng

import android.content.Context
import com.darcy.message.lib_umeng.proxy.IAgentProxy
import com.darcy.message.lib_umeng.proxy.UMengAgentProxyImpl

object ReportManager {
    private var iAgentProxy: IAgentProxy

    init {
        // use UMeng implementation by default
        iAgentProxy = UMengAgentProxyImpl
    }

    /**
     * set custom report helper
     */
    fun setReportHelper(reportHelper: IAgentProxy) {
        iAgentProxy = reportHelper
    }

    fun preInit() {
        iAgentProxy.preInit()
    }

    fun init(isDebug: Boolean) {
        iAgentProxy.init(isDebug)
    }

    /**
     * report show event
     */
    fun show(context: Context, pageName: String, params: Map<String, Any?>? = null) {
        iAgentProxy.pageStart(context, pageName, params)
    }

    /**
     * report hide event
     */
    fun hide(context: Context, pageName: String, params: Map<String, Any?>? = null) {
        iAgentProxy.pageEnd(context, pageName, params)
    }

    /**
     * report onResume event
     */
    fun onResume(context: Context, pageName: String, params: Map<String, Any?>? = null) {
        iAgentProxy.onResume(context, pageName, params)
    }

    /**
     * report pause event
     */
    fun onPause(context: Context, pageName: String, params: Map<String, Any?>? = null) {
        iAgentProxy.onPause(context, pageName, params)
    }

    /**
     * report click event
     */
    fun click(context: Context, eventName: String, params: Map<String, Any?>? = null) {
        iAgentProxy.clickEvent(context, eventName, params)
    }

    /**
     * report sign in event
     */
    fun signIn(userID: String) {
        iAgentProxy.signIn(userID)
    }

    /**
     * report sign out event
     */
    fun signOut(userID: String) {
        iAgentProxy.signOut(userID)
    }

    /**
     * report sign in event
     */
    fun signIn(provider: String, userID: String) {
        iAgentProxy.signIn(provider, userID)
    }

    /**
     * report sign out event
     */
    fun signOut(provider: String, userID: String) {
        iAgentProxy.signOut(provider, userID)
    }


}