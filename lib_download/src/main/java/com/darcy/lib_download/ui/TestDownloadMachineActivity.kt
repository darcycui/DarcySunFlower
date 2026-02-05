package com.darcy.lib_download.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.lib_download.R
import com.darcy.lib_download.databinding.LibDownloadActivityTestDownloadMachineBinding
import com.darcy.lib_download.event.DownloadEvent
import com.darcy.lib_download.event.toMessage
import com.darcy.lib_download.statemachine.DownloadStateMachine
import com.darcy.lib_download.statemachine.State

class TestDownloadMachineActivity : AppCompatActivity() {
    private val binding: LibDownloadActivityTestDownloadMachineBinding by lazy {
        LibDownloadActivityTestDownloadMachineBinding.inflate(layoutInflater)
    }

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
        initObserver()
    }

    private fun initObserver() {
        binding.apply {
            start.setOnClickListener {
                DownloadStateMachine.getInstance().sendMessage(DownloadEvent.Start.toMessage())
            }
            pause.setOnClickListener {
                DownloadStateMachine.getInstance().sendMessage(DownloadEvent.Pause.toMessage())
            }
        }
    }

    private fun initView() {
        val currentState = DownloadStateMachine.getInstance().currentState as? State
        binding.apply {
            tvState.text = currentState?.javaClass?.simpleName ?: "null"
        }
    }
}