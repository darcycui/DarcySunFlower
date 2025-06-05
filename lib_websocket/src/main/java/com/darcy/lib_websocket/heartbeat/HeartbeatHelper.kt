package com.darcy.lib_websocket.heartbeat

import com.darcy.lib_websocket.client.IWebSocketClient
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.print
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

const val PING = "ping"
const val PONG = "pong"

class HeartbeatHelper(
    private val iWebSocketClient: IWebSocketClient
) {
    companion object {
        private val TAG = HeartbeatHelper::class.java.simpleName
        private var instance: HeartbeatHelper? = null

        @Synchronized
        fun getInstance(iWebSocketClient: IWebSocketClient): HeartbeatHelper {
            if (instance == null) {
                instance = HeartbeatHelper(iWebSocketClient)
            }
            return instance!!
        }
    }

    private val dispatcher: CoroutineDispatcher = newSingleThreadContext("heartbeat")
    private val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            logE("$TAG exceptionHandler: ${throwable.message}")
            throwable.print()
        }
    private val scope: CoroutineScope =
        CoroutineScope(dispatcher + SupervisorJob() + exceptionHandler)
    private var pingJob: Job? = null
    private var pongTimeoutJob: Job? = null

    fun start(heartbeatPeriod: Long, pongTimeout: Long) {
        pingJob = scope.launch {
            // 使用 isActive 检查作用域状态 循环执行心跳 除非scope被cancel
            while (isActive) {
                delay(heartbeatPeriod)
                sendPing()
                pongTimeoutJob = scope.launch {
                    if (isActive) {
                        delay(pongTimeout)
                        dealPongTimeout()
                    }
                }
            }
        }
    }

    fun stop() {
        pingJob?.cancel()
    }

    private fun sendPing() {
        iWebSocketClient.send("ping", "")
    }

    private fun dealPongTimeout() {
        iWebSocketClient.disconnect()
    }

    fun clearPongTimeoutJob() {
        pongTimeoutJob?.cancel()
        pongTimeoutJob = null
    }
}