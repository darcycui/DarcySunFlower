package com.darcy.message.lib_umeng.agent

interface IShowAgent {
    /**
     * [pageName] is the name of the event
     * [params] is the params of the event
     */
    fun pageStart(pageName: String, params: Map<String, Any?>? = null)
    /**
     * [pageName] is the name of the event
     * [params] is the params of the event
     */
    fun pageEnd(pageName: String, params: Map<String, Any?>? = null)

}