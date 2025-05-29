package com.darcy.lib_websocket.client

import android.content.Context
import com.darcy.lib_websocket.bean.MessageBean
import com.darcy.lib_websocket.factory.OkHttpFactory
import com.darcy.lib_websocket.json.GsonTransferImpl
import com.darcy.lib_websocket.json.IJsonTransfer
import com.darcy.lib_websocket.listener.DefaultWebSocketListener
import com.darcy.lib_websocket.listener.IWebSocketListener
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okio.ByteString.Companion.toByteString
import java.nio.ByteBuffer
import java.time.LocalDateTime


class OkHttpWebSocketClient : IWebSocketClient {
    companion object {
        private val TAG = OkHttpWebSocketClient::class.java.simpleName
        private var mInstance: OkHttpWebSocketClient? = null
        fun getInstance(): OkHttpWebSocketClient {
            if (mInstance == null) {
                synchronized(OkHttpWebSocketClient::class.java) {
                    if (mInstance == null) {
                        mInstance = OkHttpWebSocketClient()
                    }
                }
            }
            return mInstance!!
        }
    }

    private var client: OkHttpClient? = null
    private var webSocket: WebSocket? = null
    private var fromUser = ""
    private var url: String = ""
    private var outerListener: IWebSocketListener? = null
    private val iJsonTransfer: IJsonTransfer = GsonTransferImpl.getInstance()

    override fun init(context: Context, url: String, fromUser: String) {
        if (client != null && webSocket != null) {
            return
        }
        this.fromUser = fromUser
        this.url = "$url/$fromUser"
        if (client == null) {
            client = OkHttpFactory.create()
        }
        logI("$TAG init")
    }

    override fun connect() {
        if (client != null && webSocket != null) {
            return
        }
        val request = Request.Builder().url(url).build()
        webSocket = client?.newWebSocket(request, DefaultWebSocketListener(this))
        logD("$TAG connect")
    }

    override fun disconnect() {
        webSocket?.close(1000, "disconnect...")
        logD("$TAG disconnect")
    }

    override fun send(message: String, toUser: String) {
        val messageBean = MessageBean(
            from = fromUser,
            to = toUser,
            message = message,
            createTime = LocalDateTime.now().toString()
        )
        val jsonMessage = iJsonTransfer.toJson(messageBean)
        webSocket?.send(jsonMessage)
        logD("$TAG send message: $jsonMessage")
    }

    override fun send(bytes: ByteArray) {
        webSocket?.send(ByteBuffer.wrap(bytes).toByteString())
    }

    override fun reconnect() {
        disconnect()
        connect()
        logD("$TAG reconnect")
    }

    override fun onOpen() {
        logD("$TAG onOpen")
        outerListener?.onOpen()
    }

    override fun onReceive(message: String) {
        logD("$TAG receive message: $message")
        outerListener?.onMessage(message)
    }

    override fun onReceive(bytes: ByteArray) {
        logD("$TAG receive bytes: ${bytes.contentToString()}")
        outerListener?.onMessage(bytes)
    }

    override fun onFailure(errorMessage: String) {
        logE("$TAG onFailure: $errorMessage")
        outerListener?.onFailure(errorMessage)
    }

    override fun onClosed() {
        logD("$TAG onClosed")
        outerListener?.onClosed()
    }

    override fun setListener(listener: IWebSocketListener) {
        this.outerListener = listener
    }
}