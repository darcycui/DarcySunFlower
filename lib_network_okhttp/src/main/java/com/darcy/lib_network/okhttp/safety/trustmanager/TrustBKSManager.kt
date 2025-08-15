package com.darcy.lib_network.okhttp.safety.trustmanager

import android.content.Context
import com.darcy.message.lib_common.exts.logD
import java.security.KeyStore
import java.util.Arrays
import java.util.function.Supplier
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * 只信任内置 bks证书(多个)
 */
object TrustBKSManager {

    // 创建合并多个 BKS 的 TrustManager
    @Throws(Exception::class)
    fun build(
        context: Context,
        bksNames: List<String>,
        passwords: List<String>
    ): X509TrustManager? {
        // 创建合并的 KeyStore
        val mergedKeyStore = KeyStore.getInstance("BKS")
        mergedKeyStore.load(null, null)

        // 加载所有 BKS 证书
        for (i in bksNames.indices) {
            val certFileName = bksNames[i]
            context.assets.open(certFileName).use { ins ->
                logD("加载证书：$certFileName")
                val ks = KeyStore.getInstance("BKS")
                ks.load(ins, passwords[i].toCharArray())
                addToKeyStore(mergedKeyStore, ks, i)
            }
        }

        // 初始化 TrustManagerFactory
        val factory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        factory.init(mergedKeyStore)


        // 获取 X509TrustManager
        return Arrays.stream<TrustManager?>(factory.trustManagers)
            .filter { tm: TrustManager? -> tm is X509TrustManager }
            .map<X509TrustManager?> { tm: TrustManager? -> tm as X509TrustManager }
            .findFirst()
            .orElseThrow<IllegalStateException?>(
                Supplier { IllegalStateException("No X509TrustManager found") }
            )
    }

    // 将 KeyStore 内容添加到目标 KeyStore
    @Throws(Exception::class)
    private fun addToKeyStore(
        target: KeyStore,
        source: KeyStore,
        prefix: Int
    ) {
        val aliases = source.aliases()
        while (aliases.hasMoreElements()) {
            val alias = aliases.nextElement()
            if (source.isCertificateEntry(alias)) {
                // 添加证书（使用唯一别名）
                target.setCertificateEntry(
                    "cert_" + prefix + "_" + alias,
                    source.getCertificate(alias)
                )
            }
            // 如需支持私钥可在此扩展
        }
    }
}