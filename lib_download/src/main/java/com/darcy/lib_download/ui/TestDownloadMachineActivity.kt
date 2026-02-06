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
import com.darcy.lib_download.listener.IStateChangeListener
import com.darcy.lib_download.listener.IStateProgressChangeListener
import com.darcy.lib_download.statemachine.DownloadStateMachine
import com.darcy.lib_download.statemachine.IState
import com.darcy.lib_download.statemachine.State

class TestDownloadMachineActivity : AppCompatActivity() {
    private val binding: LibDownloadActivityTestDownloadMachineBinding by lazy {
        LibDownloadActivityTestDownloadMachineBinding.inflate(layoutInflater)
    }
    private var currentProgress: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top + 20, systemBars.right, systemBars.bottom)
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
            resume.setOnClickListener {
                DownloadStateMachine.getInstance().sendMessage(DownloadEvent.Resume.toMessage())
            }
            updateProgress.setOnClickListener {
                DownloadStateMachine.getInstance().sendMessage(
                    DownloadEvent.ProgressUpdate(currentProgress++).toMessage()
                )
            }
        }
    }

    private fun initView() {
        DownloadStateMachine.init(
            stateCallback = object : IStateChangeListener {
                override fun onStateChange(newState: IState) {
                    runOnUiThread {
                        binding.tvState.text = newState.name
                    }
                }
            },
            progressCallback = object : IStateProgressChangeListener {
                override fun onProgressChange(newState: IState, progress: Double) {
                    runOnUiThread {
                        binding.tvState.text = newState.name
                    }
                }
            }
        )
        val currentState = DownloadStateMachine.getInstance().currentState as? State
        binding.apply {
            tvState.text = currentState?.javaClass?.simpleName ?: "null"
        }
    }

    private fun updateUI(block: () -> Unit) {
        runOnUiThread {
            block()
        }
    }
}