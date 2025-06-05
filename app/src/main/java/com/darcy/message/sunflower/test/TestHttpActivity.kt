package com.darcy.message.sunflower.test

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.toasts
import com.darcy.message.lib_http.HttpManager
import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_http.utils.DNSUtil
import com.darcy.message.lib_http.utils.NetworkTypeUtil
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.sunflower.databinding.AppActivityTestHttpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class TestHttpActivity : BaseActivity<AppActivityTestHttpBinding>() {
    private val scope: CoroutineScope by lazy {
        MainScope()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DNSUtil.registerNetworkCallback(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        DNSUtil.unregisterNetworkCallback(this)
        scope.cancel()
    }

    override fun initView() {
        binding.btnCheckHttpProxy.setOnClickListener {
            checkHttpProxy()
        }
        binding.btnHttpJuhe.setOnClickListener {
            doHttpRequest(
                "https://apis.juhe.cn", "/ip/ipNewV3", mapOf(
                    "ip" to "114.215.154.101",
                    "key" to "f128bfc760193c5762c5c3be2a6051d8"
                ), false
            )
        }
        binding.btnHttpDarcyIP.setOnClickListener {
            doHttpRequest(
                "https://10.0.0.241", "/users/all", mapOf(), false
            )
        }
        binding.btnHttpDarcyServer.setOnClickListener {
            doHttpRequest(
                "https://darcycui.com.cn", "/users/all", mapOf(), false
            )
        }
        binding.btnTestHttpEBPF.setOnClickListener {
            doEBPF()
        }
        binding.btnTestLogin.setOnClickListener {
            doLogin()
        }
        binding.btnDNS.setOnClickListener {
            doDNS()
        }
        binding.btnNetworkType.setOnClickListener {
            doNetWorkType()
        }
    }

    private fun doNetWorkType() {
        NetworkTypeUtil.getNetworkType(applicationContext).also {
            logD("网络类型 $it")
            toasts("网络类型 $it")
        }
    }

    private fun doDNS() {
        DNSUtil.getDnsServers(applicationContext, callback = { type, servers ->
            logD("type $type, servers $servers")
            toasts("type $type, servers $servers")
        })
    }

    private fun doLogin() {
        scope.launch {
            HttpManager.doGet<String>(
                baseUrl = "https://www.baidu.com",
                path = "/",
                //                    baseUrl = "https://10.0.0.200",
                //                    path = "/UniExServicesWeb/login",
                params = mapOf(),
                useCache = false
            ) {
                start {
                    println("start")
                }
                request {
                    println("request:")
                    null
                }
                success {
                    println("success:$it")
                    showResult(it.toString())
                }
                error {
                    println("error:$it")
                    showResult(it)
                }
                finish {
                    println("finish")
                }
            }
        }
    }

    private fun doEBPF() {
        scope.launch {
            HttpManager.doGet<String>(
                baseUrl = "https://api.github.com",
                path = "/search/repositories",
                params = mapOf(
                    "q" to "java"
                ),
                useCache = false
            ) {
                start {
                    println("start")
                }
                //            request {
                //                println("request:")
                //            }
                success {
                    println("success:$it")
                    showResult(it.toString())
                }
                error {
                    println("error:$it")
                    showResult(it)
                }
                finish {
                    println("finish")
                }
            }
        }
    }

    private fun doHttpRequest(
        baseUrl: String,
        path: String,
        params: Map<String, String>,
        useCache: Boolean
    ) {
        scope.launch {
            HttpManager.doGet<IPEntity>(baseUrl, path, params, useCache) {
                start {
                    println("start")
                }
                request {
                    println("request:")
                    //                        val api = JuHeApiService.api()
                    //                        api.checkIP("110.110.110.110")
                    null
                }
                success {
                    println("success:$it")
                    showResult(it.toString())
                }
                error {
                    println("error:$it")
                    showResult(it)
                }
                finish {
                    println("finish")
                }
            }
        }
    }

    override fun initListener() {

    }

    override fun initData() {

    }

    private fun showResult(it: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.tvInfo.text = it
        }
    }

    private fun checkHttpProxy() {
        val proxyHost = System.getProperty("http.proxyHost")
        val proxyPort = System.getProperty("http.proxyPort")
//        val  proxyHost = android.net.Proxy.getHost(this);
//        val  proxyPort = android.net.Proxy.getPort(this);
        Toast.makeText(this, "$proxyHost:$proxyPort", Toast.LENGTH_SHORT).show()
        binding.tvInfo.text = "httpProxy-->$proxyHost:$proxyPort"
    }
}