package com.darcy.lib_websocket.certificate

import com.darcy.lib_websocket.config.HttpConfig
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_common.exts.logW
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession

class VerifyHostHelper : HostnameVerifier {
    private val TAG = this::class.java.simpleName
    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        logW("VerifyHostHelper:verify")
        if (hostname == "apis.juhe.cn") {
            logV("hostname=$hostname ==>verify SUCCESS by default")
            return true
        }
        if (hostname == "darcycui.com.cn") {
            logV("hostname=$hostname ==>verify SUCCESS by default")
            return true
        }

        //BASE_URL
        val buildInHostName = HttpConfig.getHostName()
        val verify =
            HttpsURLConnection.getDefaultHostnameVerifier().verify(buildInHostName, session)
        logV("hostname=$hostname ==>verify:$verify")
        return verify
    }
}