package com.darcy.lib_websocket.listener

import com.darcy.lib_websocket.client.IWebSocketClient
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logW
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class DefaultWebSocketListener(
    private val iWebSocketClient: IWebSocketClient
) : WebSocketListener() {
    companion object {
        private val TAG = DefaultWebSocketListener::class.java.simpleName
    }
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        logI("$TAG onOpen: $response")
        iWebSocketClient.onOpen()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        logD("$TAG onMessage: $text")
        iWebSocketClient.onReceive(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        logD("$TAG onMessage: ${bytes.string(Charsets.UTF_8)}")
        iWebSocketClient.onReceive(bytes.toByteArray())
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        logE("$TAG onFailure: ${t.message}")
        iWebSocketClient.onFailure(t.message ?: "empty error message")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        logW("$TAG onClosing: $code, $reason")
        iWebSocketClient.disconnect()
        iWebSocketClient.onClosed()
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        logW("$TAG onClosed: $code, $reason")
        iWebSocketClient.disconnect()
        iWebSocketClient.onClosed()
    }
}