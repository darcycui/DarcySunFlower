package com.darcy.message.lib_ui.mvi.intent

import com.darcy.message.lib_ui.mvi.bean.UserBean

interface IUiIntent {
}


sealed class MainUiIntent : IUiIntent {
    data class LoginIntent(val userBean: UserBean) : MainUiIntent()
    data class LogoutIntent(val userBean: UserBean) : MainUiIntent()
    data class LoadNewsIntent(val id: Int) : MainUiIntent()
}