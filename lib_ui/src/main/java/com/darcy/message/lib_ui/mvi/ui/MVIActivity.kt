package com.darcy.message.lib_ui.mvi.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.darcy.message.lib_im.databinding.ActivityMviactivityBinding
import com.darcy.message.lib_ui.mvi.state.MainViewIntent
import com.darcy.message.lib_ui.mvi.state.MainViewEvent
import com.darcy.message.lib_ui.mvi.state.MainViewState
import com.darcy.message.lib_ui.mvi.utils.FetchStatus
import com.darcy.message.lib_ui.mvi.utils.observeEvent
import com.darcy.message.lib_ui.mvi.utils.observeState
import com.darcy.message.lib_ui.mvi.utils.toast
import com.darcy.message.lib_ui.mvi.viewmodel.MainViewModel

class MVIActivity : AppCompatActivity() {
    private val context: AppCompatActivity by lazy {
        this
    }
    private val binding: ActivityMviactivityBinding by lazy {
        ActivityMviactivityBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_mviactivity)
        setContentView(binding.root)
        initView()
        initViewModel()
    }

    private fun initView() {
        binding.btnCount.setOnClickListener {
            viewModel.dispatch(MainViewIntent.FabClickedIntent)
        }
        binding.btnLoad.setOnClickListener {
            viewModel.dispatch(MainViewIntent.FetchNewsIntent)
        }
    }

    private fun initViewModel() {
        viewModel.viewEvents.observeEvent(this) {
            when (it) {
                is MainViewEvent.ShowSnackbarEvent -> {
                    binding.tvInfo.text = "ShowSnackbar"
                }

                is MainViewEvent.ShowToastEvent -> {
                    toast(message = it.message)
                }
            }
        }

        viewModel.viewStates.run {
            observeState(context, MainViewState::newsList) {
                binding.tvInfo.text = "add to list\nadd to list\nadd to list"
            }
            observeState(context, MainViewState::fetchStatus) {
                when (it) {
                    is FetchStatus.Fetched -> {
                        binding.tvInfo.text = "Fetched"
                    }

                    is FetchStatus.NotFetched -> {
                        viewModel.dispatch(MainViewIntent.FetchNewsIntent)
                        binding.tvInfo.text = "NotFetched"
                    }

                    is FetchStatus.Fetching -> {
                        binding.tvInfo.text = "Fetching"
                    }
                }
            }
        }
    }
}