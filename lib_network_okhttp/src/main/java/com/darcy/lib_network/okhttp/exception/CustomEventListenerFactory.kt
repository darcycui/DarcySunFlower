package com.darcy.lib_network.okhttp.exception

import okhttp3.Call
import okhttp3.EventListener

class CustomEventListenerFactory : EventListener.Factory {
    override fun create(call: Call): EventListener {
        return CustomEventListener()
    }
}