package com.darcy.lib_websocket.listener

interface IWebSocketListener {
    fun onOpen()
    fun onMessage(message: String)
    fun onMessage(bytes: ByteArray)
    fun onFailure(errorMessage: String)
    fun onClosed()
}