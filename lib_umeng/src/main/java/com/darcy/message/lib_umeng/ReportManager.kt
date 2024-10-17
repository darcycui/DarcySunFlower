package com.darcy.message.lib_umeng

import com.darcy.message.lib_umeng.proxy.IReportProxy
import com.darcy.message.lib_umeng.proxy.UMengProxyImpl

object ReportManager {
    private var iReportProxy: IReportProxy

    init {
        // use UMeng implementation by default
        iReportProxy = UMengProxyImpl
    }

    /**
     * set custom report helper
     */
    fun setReportHelper(reportHelper: IReportProxy) {
        iReportProxy = reportHelper
    }

    fun preInit() {
        iReportProxy.preInit()
    }

    fun init(isDebug: Boolean) {
        iReportProxy.init(isDebug)
    }

    /**
     * report show event
     */
    fun show(pageName: String, params: Map<String, Any?>? = null) {
        iReportProxy.pageStart(pageName, params)
    }

    /**
     * report hide event
     */
    fun hide(pageName: String, params: Map<String, Any?>? = null) {
        iReportProxy.pageEnd(pageName, params)
    }

    /**
     * report click event
     */
    fun click(eventName: String, params: Map<String, Any?>? = null) {
        iReportProxy.clickEvent(eventName, params)
    }

    /**
     * report sign in event
     */
    fun signIn(userID: String) {
        iReportProxy.signIn(userID)
    }

    /**
     * report sign out event
     */
    fun signOut(userID: String) {
        iReportProxy.signOut(userID)
    }

    /**
     * report sign in event
     */
    fun signIn(provider: String, userID: String) {
        iReportProxy.signIn(provider, userID)
    }

    /**
     * report sign out event
     */
    fun signOut(provider: String, userID: String) {
        iReportProxy.signOut(provider, userID)
    }


}