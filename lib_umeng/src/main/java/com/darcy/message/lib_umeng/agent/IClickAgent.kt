package com.darcy.message.lib_umeng.agent

interface IClickAgent {
    /**
     * [eventName] is the name of the event
     * [params] is the params of the event
     */
    fun clickEvent(eventName: String, params: Map<String, Any?>? = null)

}