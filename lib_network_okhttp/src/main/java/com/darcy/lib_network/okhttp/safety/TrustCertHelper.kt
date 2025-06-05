package com.darcy.lib_network.okhttp.safety

import com.darcy.lib_network.okhttp.safety.trustmanager.TrustAllManager
import com.darcy.lib_network.okhttp.safety.trustmanager.TrustBKSManager
import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import javax.net.ssl.X509TrustManager

/**
 * 内置证书
 */
object TrustCertHelper {
    private val TAG = this::class.java.simpleName

    /**
     * create a trust for all certificates
     */
    fun createTrustAll(): X509TrustManager {
        return TrustAllManager()
    }

    /**
     * create a trust for build-in certificates only
     * should use [VerifyHostHelper] together
     */
    fun createTrustBuildIn(): X509TrustManager? {
        return runCatching {
            val certNames = listOf("juheBKS.bks", "test2IPSelfBKS137.bks", "test2IPSelfBKS241.bks","test2ServerSelfBKS.bks")
            val passwords = listOf("123456", "123456", "123456", "123456")
//            val certNames = listOf("juheBKS.bks")
//            val passwords = listOf("123456")
            TrustBKSManager.build(AppHelper.getAppContext(), certNames, passwords)
        }.onSuccess {
            logI("$TAG createTrustBuildIn: success")
        }.onFailure {
            logE("$TAG createTrustBuildIn: $it")
        }.getOrElse {
            null
        }
    }


}

