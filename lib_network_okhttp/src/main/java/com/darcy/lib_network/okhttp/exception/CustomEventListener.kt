package com.darcy.lib_network.okhttp.exception

import com.darcy.message.lib_common.exts.logE
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.Protocol
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy

class CustomEventListener : EventListener() {
    override fun connectFailed(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
        ioe: IOException
    ) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
        logE("连接异常")
        super.callEnd(call)
    }

    override fun requestFailed(call: Call, ioe: IOException) {
        super.requestFailed(call, ioe)
        logE("请求异常")
        super.callEnd(call)
    }

    override fun responseFailed(call: Call, ioe: IOException) {
        super.responseFailed(call, ioe)
        logE("响应异常")
        super.callEnd(call)
    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        logE("okhttp call 异常")
        super.callEnd(call)
    }
}