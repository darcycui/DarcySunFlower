package com.darcy.message.lib_umeng.agent

import android.content.Context

interface IShowAgent {
    /**
     * [pageName] is the name of the event
     * [params] is the params of the event
     */
    fun pageStart(context: Context, pageName: String, params: Map<String, Any?>? = null)

    /**
     * [pageName] is the name of the event
     * [params] is the params of the event
     */
    fun pageEnd(context: Context, pageName: String, params: Map<String, Any?>? = null)

}