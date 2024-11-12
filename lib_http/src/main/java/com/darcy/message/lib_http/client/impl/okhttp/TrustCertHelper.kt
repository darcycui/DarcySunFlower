package com.darcy.message.lib_http.client.impl.okhttp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logW
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


object TrustCertHelper {
    private val TAG = this::class.java.simpleName

    /**
     * create a trust for all certificates
     */
    fun createTrustAllSSLSocketFactory(): Pair<SSLSocketFactory?, X509TrustManager?> {
        var first: SSLSocketFactory? = null
        var second: X509TrustManager? = null
        try {
            val sslContext = SSLContext.getInstance("TLS")
            second = TrustAllManager()
            sslContext.init(null, arrayOf<TrustManager>(second), SecureRandom())
            first = sslContext.socketFactory
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
        return Pair(first, second)
    }

    /**
     * create a trust for built in certificate only
     */
    fun createTrustBuildInSSLSocketFactoryPair(context: Context): Pair<SSLSocketFactory?, X509TrustManager?> {
        try {
            var first: SSLSocketFactory? = null
            var second: X509TrustManager? = null


            //1、获取证书 keyStore
            var keyStore: KeyStore? = null
//            val bksName = "darcyBKS.bks"
//            val bksName = "weixinBKS.bks"
            val bksName = "login_10.bks"
            //证书密码（没有密码传 null）
            val bksPassword = "123456"
            context.resources.assets.open(bksName).use { inputStream ->
                keyStore = KeyStore.getInstance("BKS")
                keyStore?.load(inputStream, bksPassword.toCharArray())
            }
            //2、获取校验证书用的 trustManager
            var trustManager: X509TrustManager? = null
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            val trustManagers = trustManagerFactory.trustManagers
            check(!(trustManagers.isEmpty() || trustManagers[0] !is X509TrustManager)) {
                "TrustManager error:" + trustManagers.contentToString()
            }
            trustManager = trustManagers[0] as? X509TrustManager


            //3、构建内置证书校验 trustManagers
            val finalTrustManager = trustManager!!
            val finalTrustManagers = arrayOf<X509TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    x509Certificates: Array<X509Certificate>,
                    s: String
                ) {
                    logW("finalTrustManager:checkClientTrusted")
                    //使用内置证书，校验客户端证书 （https 双向校验时）
                    finalTrustManager.checkClientTrusted(x509Certificates, s)
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    x509Certificates: Array<X509Certificate>,
                    s: String
                ) {
                    logW("finalTrustManager:checkServerTrusted")
                    // 遍历证书链
                    for (certificate in x509Certificates) {
                        logD("certificate.issuerDN.name=${certificate.issuerDN.name} certificate.serialNumber=${certificate.issuerDN.name}")
                    }
                    //使用内置证书，校验服务端证书
                    finalTrustManager.checkServerTrusted(x509Certificates, s)
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })
            second = finalTrustManagers[0]


            //4.配置 trustManagers
            var sslContext: SSLContext? = null
            sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, finalTrustManagers, SecureRandom())
            if (sslContext != null) {
                first = sslContext.socketFactory
            } else {
                throw IllegalStateException("SSLContext error")
            }
            return Pair(first, second)
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(null, null)
    }
}


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

