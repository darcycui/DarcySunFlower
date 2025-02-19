package com.darcy.message.lib_ui.mvi.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.lib_ui.databinding.LibUiActivityMviactivityBinding
import com.darcy.message.lib_ui.exts.showSnackBar
import com.darcy.message.lib_ui.mvi.bean.UserBean
import com.darcy.message.lib_ui.mvi.intent.MainUiIntent
import com.darcy.message.lib_ui.mvi.state.LoginUiState
import com.darcy.message.lib_ui.mvi.state.NewsUiState
import com.darcy.message.lib_ui.mvi.viewmodel.MainViewModel2
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MVIActivity : BaseActivity<LibUiActivityMviactivityBinding>() {

    private val viewModel2: MainViewModel2 by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
    }

    override fun initListener() {
        binding.btnLogin.setOnClickListener {
            viewModel2.sendUiIntent(MainUiIntent.LoginIntent(UserBean(1, "darcy", "123456")))
            showSnackBar("This is a snackbar")
        }
        binding.btnLogout.setOnClickListener {
            viewModel2.sendUiIntent(MainUiIntent.LogoutIntent(UserBean(1, "darcy", "123456")))
            showSnackBar("This is a snackbar")
        }
        binding.btnLoadNews.setOnClickListener {
            viewModel2.sendUiIntent(MainUiIntent.LoadNewsIntent(id = 1))
        }
        initObservers()
    }

    override fun initData() {
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel2.uiStateFlow.map { it.loginUiState }.distinctUntilChanged().collect {
                when(it){
                    is LoginUiState.Loading -> {
                        binding.tvInfo.text = "Loading"
                    }

                    is LoginUiState.Success -> {
                        binding.tvInfo.text = "Success ${it.data}"
                    }

                    is LoginUiState.Error -> {
                        binding.tvInfo.text = "Error ${it.error}"
                    }

                    is LoginUiState.SignedOut -> {
                        binding.tvInfo.text = "SignedOut"
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel2.uiStateFlow.map { it.newsUiState }.distinctUntilChanged().collect{
                when(it){
                    is NewsUiState.Loading -> {
                        binding.tvNews.text = "Loading"
                    }

                    is NewsUiState.Success -> {
                        binding.tvNews.text = "Success ${it.data}"
                    }

                    is NewsUiState.Error -> {
                        binding.tvNews.text = "Error ${it.error}"
                    }
                }
            }
        }
    }
}