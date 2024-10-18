package com.darcy.message.lib_umeng.agent

import android.content.Context

interface IClickAgent {
    /**
     * [eventName] is the name of the event
     * [params] is the params of the event
     */
    fun clickEvent(context: Context, eventName: String, params: Map<String, Any?>? = null)

}