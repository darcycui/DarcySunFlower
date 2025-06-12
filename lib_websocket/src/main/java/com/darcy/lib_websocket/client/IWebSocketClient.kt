package com.darcy.lib_websocket.client

import android.content.Context
import com.darcy.lib_websocket.listener.IOuterListener

interface IWebSocketClient {
    fun init(context: Context, url: String, fromUser: String)
    fun connect()
    fun disconnect()
    fun send(message: String, toUser: String)
    fun send(bytes: ByteArray)
    fun reconnect()
    fun setOuterListener(listener: IOuterListener)
}