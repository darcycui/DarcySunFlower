package com.darcy.lib_websocket

import android.content.Context
import com.darcy.lib_websocket.client.IWebSocketClient
import com.darcy.lib_websocket.client.OkHttpWebSocketClient
import com.darcy.lib_websocket.listener.IOuterListener

object WebsocketManager {
    private val TAG = WebsocketManager::class.java.simpleName
    private val iWebSocketClient: IWebSocketClient = OkHttpWebSocketClient.getInstance()

    //初始化
    fun init(context: Context, url: String, fromUser: String) {
        iWebSocketClient.init(context, url, fromUser)
    }

    //连接
    fun connect() {
        iWebSocketClient.connect()
    }

    //断开
    fun disconnect() {
        iWebSocketClient.disconnect()
    }

    //发送
    fun send(message: String, toUser: String) {
        iWebSocketClient.send(message, toUser)
    }

    //发送
    fun send(bytes: ByteArray) {
        iWebSocketClient.send(bytes)
    }

    //重连
    fun reconnect() {
        iWebSocketClient.reconnect()
    }

    //设置外部监听
    fun setOuterListener(listener: IOuterListener) {
        iWebSocketClient.setOuterListener(listener)
    }

}