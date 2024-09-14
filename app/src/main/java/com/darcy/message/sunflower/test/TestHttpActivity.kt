package com.darcy.message.sunflower.test

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.darcy.message.lib_http.HttpManager
import com.darcy.message.lib_http.client.impl.OKHttpHttpClient
import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.sunflower.databinding.AppActivityTestHttpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TestHttpActivity : BaseActivity<AppActivityTestHttpBinding>() {
    private val scope: CoroutineScope by lazy {
        MainScope()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        binding.btnCheckHttpProxy.setOnClickListener {
            checkHttpProxy()
        }
        binding.btnOKHttp.setOnClickListener {
            scope.launch {
                HttpManager.doGet<IPEntity>(
//            baseUrl = "https://apis.juhe.cn",
                    path = "/ip/ipNewV3",
                    params = mapOf(
                        "ip" to "114.215.154.101",
                        "key" to "f128bfc760193c5762c5c3be2a6051d8"
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
        binding.btnTestHttpEBPF.setOnClickListener {
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