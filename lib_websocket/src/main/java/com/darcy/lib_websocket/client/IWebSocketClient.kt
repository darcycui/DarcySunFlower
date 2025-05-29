package com.darcy.lib_websocket.client

import android.content.Context
import com.darcy.lib_websocket.listener.IWebSocketListener

interface IWebSocketClient {
    fun init(context: Context, url: String, fromUser: String)
    fun connect()
    fun disconnect()
    fun send(message: String, toUser: String)
    fun send(bytes: ByteArray)
    fun reconnect()
    fun onOpen()
    fun onReceive(message: String)
    fun onReceive(bytes: ByteArray)
    fun onFailure(errorMessage: String)
    fun onClosed()
    fun setListener(listener: IWebSocketListener)
}