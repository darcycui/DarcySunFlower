package com.darcy.message.lib_ui.base.mvp

interface IPresenter<V : IView> {
    fun onCreate()
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onAttachView(view: V)
    fun onDetachView()

    fun getView(): V?
    fun onDestroy()
}