package com.darcy.message.lib_ui.mvi.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.darcy.message.lib_common.exts.toast
import com.darcy.message.lib_im.databinding.LibUiActivityMviactivityBinding
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.lib_ui.exts.showSnackBar
import com.darcy.message.lib_ui.mvi.bean.NewsItem
import com.darcy.message.lib_ui.mvi.state.MainViewIntent
import com.darcy.message.lib_ui.mvi.state.MainViewEvent
import com.darcy.message.lib_ui.mvi.state.MainViewState
import com.darcy.message.lib_ui.mvi.state.common.FetchStatus
import com.darcy.message.lib_ui.exts.observeEvent
import com.darcy.message.lib_ui.exts.observeState
import com.darcy.message.lib_ui.mvi.viewmodel.MainViewModel

class MVIActivity : BaseActivity<LibUiActivityMviactivityBinding>() {
    private val context: AppCompatActivity by lazy {
        this
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_mviactivity)
        setContentView(binding.root)
        initView()
        initObservers()
    }

    private fun initView() {
        binding.btnShowSnackbar.setOnClickListener {
            viewModel.dispatch(
                MainViewIntent.NewsItemClickedIntent(
                    NewsItem("title", "description", "imageUrl")
                )
            )
        }
        binding.btnCount.setOnClickListener {
            viewModel.dispatch(MainViewIntent.FabClickedIntent)
        }
        binding.btnLoad.setOnClickListener {
            viewModel.dispatch(MainViewIntent.FetchNewsIntent)
        }
    }

    private fun initObservers() {
        // observe events
        viewModel.viewEvents.observeEvent(this) {
            when (it) {
                is MainViewEvent.ShowSnackbarEvent -> {
                    showSnackBar(it.message)
                }

                is MainViewEvent.ShowToastEvent -> {
                    toast(message = it.message)
                }
            }
        }

        // observe states
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