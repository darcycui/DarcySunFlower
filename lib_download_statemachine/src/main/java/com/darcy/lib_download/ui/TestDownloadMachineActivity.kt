package com.darcy.lib_download.ui

import android.os.Bundle
import android.os.Message
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.lib_download.R
import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.lib_download.databinding.LibDownloadActivityTestDownloadMachineBinding
import com.darcy.lib_download.event.AppInstallEvent
import com.darcy.lib_download.event.toMessage
import com.darcy.lib_download.listener.IStateChangeListener
import com.darcy.lib_download.listener.IStateProgressChangeListener
import com.darcy.lib_download.state.InitState
import com.darcy.lib_download.statemachine.AppInstallStateMachine
import com.darcy.lib_download.statemachine.IState
import com.darcy.lib_download.statemachine.State

class TestDownloadMachineActivity : AppCompatActivity() {
    private val binding: LibDownloadActivityTestDownloadMachineBinding by lazy {
        LibDownloadActivityTestDownloadMachineBinding.inflate(layoutInflater)
    }
    private lateinit var stateMachine: AppInstallStateMachine
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
        stateMachine = AppInstallStateMachine(
            firstStateClass = InitState::class,
            stateCallback = object : IStateChangeListener {
                override fun onStateChanged(
                    task: AppInstallTask,
                    currentState: IState,
                    message: Message?
                ) {
                    runOnUiThread {
                        binding.tvState.text = currentState.name
                    }
                }

                override fun onStatePreChange(
                    task: AppInstallTask,
                    currentState: IState,
                    message: Message?
                ) {

                }
            },
            progressCallback = object : IStateProgressChangeListener {
                override fun onProgressChange(
                    task: AppInstallTask,
                    newState: IState,
                    progress: Double
                ) {
                    runOnUiThread {
                        binding.tvState.text = newState.name
                    }
                }
            }
        )
        stateMachine.start()
        initView()
        initObserver()
    }

    private fun initObserver() {
        binding.apply {
            start.setOnClickListener {
                stateMachine.sendMessage(AppInstallEvent.StartDownload.toMessage())
            }
            pause.setOnClickListener {
                stateMachine.sendMessage(AppInstallEvent.PauseDownload.toMessage())
            }
            resume.setOnClickListener {
                stateMachine.sendMessage(AppInstallEvent.ResumeDownload.toMessage())
            }
            updateProgress.setOnClickListener {
                stateMachine.sendMessage(
                    AppInstallEvent.UpdateProgressDownload(++currentProgress).toMessage()
                )
            }
        }
    }

    private fun initView() {
        val currentState = stateMachine.currentState as? State
        binding.apply {
            tvState.text = currentState?.javaClass?.simpleName ?: "null"
        }
    }

    private fun updateUI(block: () -> Unit) {
        runOnUiThread {
            block()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stateMachine.quitNow()
    }
}