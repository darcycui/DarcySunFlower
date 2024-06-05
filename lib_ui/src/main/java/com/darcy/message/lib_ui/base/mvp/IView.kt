package com.darcy.message.lib_ui.base.mvp

interface IView {
    fun showLoading()
    fun hideLoading()
    fun showError(msg: String)
    fun showEmpty()
    fun showContent()
    fun showToast(msg: String)
}