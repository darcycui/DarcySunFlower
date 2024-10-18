package com.darcy.message.lib_umeng.agent

import android.content.Context

interface IDurationAgent {
    /**
     * [pageName] is the name of the event
     * [params] is the params of the event
     */
    fun onResume(context: Context, pageName: String, params: Map<String, Any?>? = null)

    /**
     * [pageName] is the name of the event
     * [params] is the params of the event
     */
    fun onPause(context: Context, pageName: String, params: Map<String, Any?>? = null)

}