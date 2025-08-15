package com.darcy.lib_network.okhttp.safety

import com.darcy.lib_network.okhttp.config.HttpConfig
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_common.exts.logW
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession

/**
 * 域名校验
 */
class VerifyHostHelper : HostnameVerifier {
    companion object {
        private val TAG = this::class.java.simpleName
    }

    private val whiteList: List<String> = listOf(
        "apis.juhe.cn",
        "api.github.com",
        "darcycui.com.cn",
    )

    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        logW("$TAG verify")
        if (hostname == null || hostname.isEmpty() || hostname.isBlank()) {
            logW("$TAG hostname is null")
            return false
        }
        if (hostname in whiteList) {
            logV("$TAG hostname=$hostname ==>verify SUCCESS by default")
            return true
        }

        //BASE_URL
        val buildInHostName = HttpConfig.getHostName()
        val verify =
            HttpsURLConnection.getDefaultHostnameVerifier().verify(buildInHostName, session)
        logV("$TAG hostname=$hostname ==>verify:$verify")
        return verify
    }

    /**
     * 局域网IP段
     */
    fun isPrivateIPv4(ip: String): Boolean {
        return try {
            // 拆分 IP 地址为四个部分
            val parts = ip.split('.').map { it.toInt() }
            if (parts.size != 4 || parts.any { it !in 0..255 }) return false

            // 将 IP 转换为 32 位整型
            val ipInt = (parts[0] shl 24) or (parts[1] shl 16) or (parts[2] shl 8) or parts[3]

            when (ipInt) {
                // 10.0.0.0/8 (10.0.0.0 - 10.255.255.255)
                in 0x0A000000..0x0AFFFFFF -> true

                // 172.16.0.0/12 (172.16.0.0 - 172.31.255.255)
                in 0xAC100000..0xAC1FFFFF -> true

                // 192.168.0.0/16 (192.168.0.0 - 192.168.255.255)
                in 0xC0A80000..0xC0A8FFFF -> true

                // 回环地址 127.0.0.0/8
                in 0x7F000000..0x7FFFFFFF -> true

                // 链路本地地址 169.254.0.0/16
                in 0xA9FE0000..0xA9FEFFFF -> true
                else -> false
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            false // 非法格式直接返回 false
        }
    }

}