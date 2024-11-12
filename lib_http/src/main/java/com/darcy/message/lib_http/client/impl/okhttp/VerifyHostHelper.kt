package com.darcy.message.lib_http.client.impl.okhttp

import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_common.exts.logW
import com.darcy.message.lib_http.config.HttpConfig
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession

class VerifyHostHelper : HostnameVerifier {
    private val TAG = this::class.java.simpleName
    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        logW("VerifyHostHelper:verify")
        if (hostname == "10.0.0.200") {
            logV("hostname=$hostname ==>verify SUCCESS by default")
            return true
        }
        if (hostname == "www.baidu.com") {
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