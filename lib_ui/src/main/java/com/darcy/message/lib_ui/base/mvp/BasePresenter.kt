package com.darcy.message.lib_ui.base.mvp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

abstract class BasePresenter<T : IView> : IPresenter<T> {
    private var iView: T? = null
    private val mainScope: CoroutineScope = MainScope()

    override fun onCreate() {
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onAttachView(view: T) {
        this.iView = view
    }

    override fun getView(): T? {
        return iView
    }

    override fun onDetachView() {
        this.iView = null
    }

    override fun onDestroy() {
    }

    fun ui(block: () -> Unit) {
        mainScope.launch(Dispatchers.Main) {
            block()
        }
    }

    fun io(block: () -> Unit) {
        mainScope.launch(Dispatchers.IO) {
            block()
        }
    }

}