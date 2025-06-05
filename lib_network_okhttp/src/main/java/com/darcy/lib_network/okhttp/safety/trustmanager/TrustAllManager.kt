package com.darcy.lib_network.okhttp.safety.trustmanager

import android.annotation.SuppressLint
import android.util.Base64
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logW
import java.security.MessageDigest
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager


/**
 * trust all certificates
 */
@SuppressLint("CustomX509TrustManager")
class TrustAllManager : X509TrustManager {
    @SuppressLint("TrustAllX509TrustManager")
    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
        // do nothing
        logW("TrustAllManager:checkClientTrusted")
    }

    @SuppressLint("TrustAllX509TrustManager")
    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
        // do nothing
        logW("TrustAllManager:checkServerTrusted")
        chain?.let {
            for (cert in chain) {
                val certificate = cert as X509Certificate
                // 计算 SHA-256 指纹
                val md = MessageDigest.getInstance("SHA-256")
                val digestCA = md.digest(certificate.encoded)
                // 转换为 Base64 编码
                val fingerprintCA = Base64.encodeToString(digestCA, Base64.DEFAULT)
                logD("证书指纹: $fingerprintCA")
                // 提取公钥
                val publicKey = certificate.publicKey.encoded
                // 计算 SHA-256 指纹
                val digestPub = md.digest(publicKey)
                // 转换为 Base64 编码
                val fingerprintPub = Base64.encodeToString(digestPub, Base64.DEFAULT)
                logD("公钥指纹: $fingerprintPub")
            }
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }
}