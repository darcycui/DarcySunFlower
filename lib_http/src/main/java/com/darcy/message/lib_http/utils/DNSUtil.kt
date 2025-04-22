package com.darcy.message.lib_http.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.darcy.message.lib_common.exts.logE

/**
 * 监听网络状态
 */
object DNSUtil {
    private var networkCallback: NetworkCallback? = null

    /**
     * 获取 DNS 信息
     */
    fun getDnsServers(context: Context, callback:(String, List<String?>)-> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getDnsServersNew(context, callback)
        } else {
            getDnsServersOld(context)
        }
    }

    /**
     * 获取 DNS 信息
     * 6.0及以上使用
     */
    private fun getDnsServersNew(context: Context, callback:(String, List<String?>)-> Unit) {
        initNetworkCallback(context, callback)
        networkCallback?.let {
            registerNetworkCallback(context)
        } ?: run {
            logE("networkCallback is null")
        }
    }

    /**
     * 初始化监听回调
     */
    private fun initNetworkCallback(context: Context, callback: (String, List<String?>) -> Unit) {
        if (networkCallback != null) {
            return
        }
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            private val networks = mutableMapOf<Network, Pair<String, List<String>>>()

            val connectivityManager =
                context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            override fun onAvailable(network: Network) {
                // 网络可用时触发
                updateNetworkInfo(network, connectivityManager,callback)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities
            ) {
                // 网络能力变化时触发
                updateNetworkInfo(network, connectivityManager, callback)
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                // DNS 变化时触发
                updateNetworkInfo(network, connectivityManager, callback)
            }

            override fun onLost(network: Network) {
                // 网络断开时移除
                networks.remove(network)
            }
        }
    }

    /**
     * 注册网络变化监听
     */
    fun registerNetworkCallback(
        context: Context,
        callback: NetworkCallback? = networkCallback
    ) {
        if (callback != null) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder().build(),
                callback
            )
        } else {
            logE("callback is null")
        }
    }

    /**
     * 取消注册网络回调
     */
    fun unregisterNetworkCallback(context: Context, callback: NetworkCallback? = networkCallback) {
        if (callback != null) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(callback)
        } else {
            logE("callback is null")
        }
    }

    /**
     * 获取DNS
     */
    private fun updateNetworkInfo(
        network: Network,
        connectivityManager: ConnectivityManager,
        callback: (String, List<String?>) -> Unit
    ) {
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val linkProperties = connectivityManager.getLinkProperties(network)
        if (capabilities == null || linkProperties == null) return
        // 获取网络类型（Wi-Fi/Mobile）
        val type = when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Mobile"
            else -> "Other"
        }
        // 提取 DNS 地址
        val dnsServers = linkProperties.dnsServers.map { it.hostAddress }
        callback.invoke(type, dnsServers)
    }

    /**
     * 获取网络 DNS 信息
     * 6.0及以下版本使用
     */
    fun getDnsServersOld(context: Context): Map<String, MutableList<String>> {
        val dnsMap: MutableMap<String, MutableList<String>> = HashMap()
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            ?: return dnsMap

        // 遍历所有网络
        val networks = cm.allNetworks
        for (network in networks) {
            val linkProperties = cm.getLinkProperties(network)
            val capabilities = cm.getNetworkCapabilities(network)

            if (linkProperties == null || capabilities == null) continue

            // 获取 DNS 服务器列表
            val dnsServers: MutableList<String> = ArrayList()
            for (dns in linkProperties.dnsServers) {
                dns.hostAddress?.let { dnsServers.add(it) }
            }

            // 判断网络类型
            var networkType = "Unknown"
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                networkType = "Wi-Fi"
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                networkType = "Mobile"
            }

            // 合并同一类型网络的 DNS（例如多个 Wi-Fi 接口）
            if (dnsMap.containsKey(networkType)) {
                dnsMap[networkType]?.addAll(dnsServers)
            } else {
                dnsMap[networkType] = dnsServers
            }
        }

        return dnsMap
    }
}