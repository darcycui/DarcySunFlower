package com.darcy.message.sunflower.test

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.message.lib_http.client.impl.OKHttpClient
import com.darcy.message.lib_http.entity.IPEntity
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.sunflower.R
import com.darcy.message.sunflower.databinding.AppActivityTestHttpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TestHttpActivity : BaseActivity<AppActivityTestHttpBinding>() {
    private val scope: CoroutineScope by lazy {
        MainScope()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.btnOKHttp.setOnClickListener {
            scope.launch {
                OKHttpClient.doGet<IPEntity>(
//            baseUrl = "https://apis.juhe.cn",
                    path = "/ip/ipNewV3",
                    params = mapOf(
                        "ip" to "114.215.154.101",
                        "key" to "f128bfc760193c5762c5c3be2a6051d8"
                    ),
                    useCache = true
                ) {
                    start {
                        println("start")
                    }
//            request {
//                println("request:")
//            }
                    success {
                        println("success:$it")
                    }
                    error {
                        println("error:$it")
                    }
                    finish {
                        println("finish")
                    }
                }
            }
        }
    }
}