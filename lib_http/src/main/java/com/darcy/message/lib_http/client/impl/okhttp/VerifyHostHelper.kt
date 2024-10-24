package com.darcy.message.lib_http.client.impl.okhttp

import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_http.config.HttpConfig
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession

class VerifyHostHelper : HostnameVerifier {
    private val TAG = this::class.java.simpleName
    override fun verify(hostname: String?, session: SSLSession?): Boolean {

        //BASE_URL
        val buildInHostName = HttpConfig.getHostName()
        val verify = HttpsURLConnection.getDefaultHostnameVerifier().verify(buildInHostName, session)
        logV("hostname：$hostname verify：$verify")
        return verify
    }
}