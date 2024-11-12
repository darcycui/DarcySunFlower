package com.darcy.message.lib_http.client.impl.okhttp

import okhttp3.CertificatePinner

object CertPinnerHelper {
    /**
     * 证书锁定
     * 不支持锁定自签名证书
     * 1.use "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAA=" for default
     * 2.get real pins from Exception in logcat
     * javax.net.ssl.SSLPeerUnverifiedException: Certificate pinning failure!
     *  * Peer certificate chain:
     * 3.add real pins
     */
    fun createCertPinner(): CertificatePinner {
        return CertificatePinner.Builder()
            // login cert pub key hash base64 (不支持)
            .add("10.0.0.200", "sha256/90k6EMKvINngUIR2pNH5MzSTXS5EfdahEKtclMDpjro=")
            // juhe cert pub key hash base64
            .add("apis.juhe.cn", "sha256/vR+S2ESqWGv6QxHQeVNcmaJjLfdhO4BVzS3GgrFU0BA=")
            .add("apis.juhe.cn", "sha256/lPraBjD+VHks5/sVEDOczhg00z9TGoGMAjndDyYGUNU=")
            // baidu default
            .add("www.baidu.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            .build()
    }
}