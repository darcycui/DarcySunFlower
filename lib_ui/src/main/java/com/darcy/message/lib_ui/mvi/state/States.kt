package com.darcy.message.lib_ui.mvi.state

import com.darcy.message.lib_ui.mvi.bean.UserBean

interface IUiState {}

data class MainUiState(
    val loginUiState: LoginUiState,
    val newsUiState: NewsUiState
) : IUiState {

}

sealed class LoginUiState : IUiState {
    data object SignedOut : LoginUiState()
    data object Loading : LoginUiState()
    data class Success(val data: UserBean) : LoginUiState()
    data class Error(val error: Throwable) : LoginUiState()
}

sealed class NewsUiState : IUiState {
    data object Loading : NewsUiState()
    data class Success(val data: List<String>) : NewsUiState()
    data class Error(val error: Throwable) : NewsUiState()
}
