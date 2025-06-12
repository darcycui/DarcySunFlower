package com.darcy.lib_websocket.listener

interface IOuterListener {
    fun onOpen()
    fun onSend(message: String)
    fun onSend(bytes: ByteArray)
    fun onMessage(message: String)
    fun onMessage(bytes: ByteArray)
    fun onFailure(errorMessage: String)
    fun onClosed()
}