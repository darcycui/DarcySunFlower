package com.darcy.message.lib_ui.mvi.viewmodel

import androidx.lifecycle.viewModelScope
import com.darcy.message.lib_ui.base.BaseViewModel
import com.darcy.message.lib_ui.mvi.bean.UserBean
import com.darcy.message.lib_ui.mvi.intent.IUiIntent
import com.darcy.message.lib_ui.mvi.intent.MainUiIntent
import com.darcy.message.lib_ui.mvi.state.LoginUiState
import com.darcy.message.lib_ui.mvi.state.MainUiState
import com.darcy.message.lib_ui.mvi.state.NewsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel2 : BaseViewModel<MainUiState, MainUiIntent>() {

    override fun initUiState(): MainUiState {
        return MainUiState(loginUiState = LoginUiState.SignedOut, newsUiState = NewsUiState.Loading)
    }

    override fun handleUiIntent(intent: MainUiIntent) {
        when (intent) {

            is MainUiIntent.LoginIntent -> {
                doLogin(intent.userBean)
            }

            is MainUiIntent.LogoutIntent -> {
                doLogout(intent.userBean)
            }

            is MainUiIntent.LoadNewsIntent -> {
                fetchNews()
            }
        }
    }


    private fun doLogin(userBean: UserBean) {
        sendUiState { copy(loginUiState = LoginUiState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            delay(3_000)
            withContext(Dispatchers.Main) {
                val user = UserBean(102L, "darcy2", "123456")
                sendUiState { copy(loginUiState = LoginUiState.Success(user)) }
            }
        }
    }

    private fun doLogout(userBean: UserBean) {
        sendUiState { copy(loginUiState = LoginUiState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            delay(3_000)
            withContext(Dispatchers.Main) {
                val user = UserBean(102L, "darcy2", "123456")
                sendUiState { copy(loginUiState = LoginUiState.SignedOut) }
            }
        }
    }

    private fun fetchNews() {
        sendUiState { copy(newsUiState = NewsUiState.Loading) }
        viewModelScope.launch {
            delay(3_000)
            sendUiState {
                copy(
                    newsUiState = NewsUiState.Success(
                        listOf(
                            "news1:This is news one",
                            "news2:This is news two",
                            "news3:This is news three"
                        )
                    )
                )
            }
        }
    }

}