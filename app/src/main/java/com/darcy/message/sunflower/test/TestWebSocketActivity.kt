package com.darcy.message.sunflower.test

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.darcy.lib_websocket.WebsocketManager
import com.darcy.lib_websocket.listener.IOuterListener
import com.darcy.message.sunflower.R
import com.darcy.message.sunflower.databinding.AppActivityTestWebSocketBinding
import kotlinx.coroutines.launch

class TestWebSocketActivity : AppCompatActivity() {
    private val binding: AppActivityTestWebSocketBinding by lazy {
        AppActivityTestWebSocketBinding.inflate(layoutInflater)
    }
    private var count = 0
    private val fromUser = "Android"
    private val toUser = "three-to-android"
    private val url = "wss://10.0.0.241:7443/person"
//    private val url = "wss://darcycui.com.cn:7443/person"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        WebsocketManager.disconnect()
    }

    private fun initData() {
        WebsocketManager.init(this, url, fromUser)
        WebsocketManager.setOuterListener(object : IOuterListener {
            override fun onOpen() {
                setupUI(" 已连接")
            }

            override fun onMessage(message: String) {
                setupUI("${binding.tvInfo.text}\n 收到: $message")
            }

            override fun onMessage(bytes: ByteArray) {
            }

            override fun onFailure(errorMessage: String) {
                setupUI("${binding.tvInfo.text}\n $errorMessage")
            }

            override fun onClosed() {
                setupUI("${binding.tvInfo.text}\n 已断开")
            }
        })
    }

    private fun setupUI(text: String) {
        lifecycleScope.launch {
            binding.tvInfo.text = text
        }
    }

    private fun initView() {
        binding.apply {
            btnConnect.setOnClickListener {
                WebsocketManager.connect()
            }
            btnSend.setOnClickListener {
                count++
                WebsocketManager.send("hello-$count", toUser)
                setupUI("${binding.tvInfo.text}\n 发送: hello-$count")
            }
            btnDisconnect.setOnClickListener {
                WebsocketManager.disconnect()
            }
        }
    }
}