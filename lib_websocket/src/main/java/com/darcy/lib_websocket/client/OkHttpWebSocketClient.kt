package com.darcy.lib_websocket.client

import android.content.Context
import com.darcy.lib_network.okhttp.factory.OkHttpFactory
import com.darcy.lib_websocket.bean.MessageBean
import com.darcy.lib_websocket.heartbeat.HeartbeatHelper
import com.darcy.lib_websocket.heartbeat.PING
import com.darcy.lib_websocket.heartbeat.PONG
import com.darcy.lib_websocket.json.GsonTransferImpl
import com.darcy.lib_websocket.json.IJsonTransfer
import com.darcy.lib_websocket.listener.DefaultInnerListener
import com.darcy.lib_websocket.listener.IOuterListener
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logV
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okio.ByteString.Companion.toByteString
import java.nio.ByteBuffer
import java.time.LocalDateTime


class OkHttpWebSocketClient : IWebSocketClient, IOuterListener {
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
    private var outerListener: IOuterListener? = null
    private val iJsonTransfer: IJsonTransfer = GsonTransferImpl.getInstance()
    private val heartbeatHelper = HeartbeatHelper.getInstance(this)

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

    private fun setupWebsocket(url: String) {
        if (client == null) {
            client = OkHttpFactory.create()
        }
        if (webSocket == null) {
            val request = Request.Builder().url(url).build()
            webSocket = client?.newWebSocket(request, DefaultInnerListener(this))
        }
    }

    override fun connect() {
        if (client != null && webSocket != null) {
            logD("$TAG already connected")
            return
        }
        setupWebsocket(url)
        logD("$TAG connect")
    }

    override fun disconnect() {
        webSocket?.let {
            it.close(1000, "normal disconnect...")
            webSocket = null
        }
        logD("$TAG disconnect")
    }

    override fun send(message: String, toUser: String) {
        if (webSocket == null) {
            logE("websocket is null.")
            return
        }
        if (PING == message) {
            logV("$TAG send ping...")
            webSocket?.send(message)
            onSend(message)
            return
        }
        val messageBean = MessageBean(
            from = fromUser,
            to = toUser,
            message = message,
            createTime = LocalDateTime.now().toString()
        )
        val jsonMessage = iJsonTransfer.toJson(messageBean)
        logD("$TAG send message: $jsonMessage")
        webSocket?.send(jsonMessage)
        onSend(jsonMessage)
    }

    override fun send(bytes: ByteArray) {
        webSocket?.send(ByteBuffer.wrap(bytes).toByteString())
        onSend(bytes)
    }

    override fun reconnect() {
        disconnect()
        connect()
        logD("$TAG reconnect")
    }

    override fun onOpen() {
        logD("$TAG onOpen")
        outerListener?.onOpen()
        heartbeatHelper.start(10_000, 5_000)
    }

    override fun onSend(message: String) {
        outerListener?.onSend(message)
    }

    override fun onSend(bytes: ByteArray) {
        outerListener?.onSend(bytes)
    }

    override fun onMessage(message: String) {
        if (PONG == message) {
            logV("$TAG receive pong...")
            heartbeatHelper.clearPongTimeoutJob()
        }
        logD("$TAG receive message: $message")
        outerListener?.onMessage(message)
    }

    override fun onMessage(bytes: ByteArray) {
        logD("$TAG receive bytes: ${bytes.contentToString()}")
        outerListener?.onMessage(bytes)
    }

    override fun onFailure(errorMessage: String) {
        logE("$TAG onFailure: $errorMessage")
        outerListener?.onFailure(errorMessage)
        webSocket = null
        heartbeatHelper.stop()
    }

    override fun onClosed() {
        logD("$TAG onClosed")
        outerListener?.onClosed()
        webSocket = null
        heartbeatHelper.stop()
    }

    override fun setOuterListener(listener: IOuterListener) {
        this.outerListener = listener
    }
}