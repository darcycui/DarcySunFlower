package com.darcy.message.lib_ui.base.mvp

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.darcy.message.lib_ui.base.BaseActivity

abstract class BaseMVPActivity<VB : ViewBinding, V : IView, P : IPresenter<V>> : BaseActivity<VB>(),
    IView {
    private val _presenter: P by lazy { createPresenter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPresenter().onCreate()
        // darcyRefactor cast this to V
        getPresenter().onAttachView(this as V)
    }

    override fun onStart() {
        super.onStart()
        getPresenter().onStart()
    }

    override fun onResume() {
        super.onResume()
        getPresenter().onResume()
    }

    override fun onPause() {
        super.onPause()
        getPresenter().onPause()
    }

    override fun onStop() {
        super.onStop()
        getPresenter().onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        getPresenter().onDetachView()
        getPresenter().onDestroy()
    }

    protected fun getPresenter(): P {
        return _presenter
    }

    abstract fun createPresenter(): P
}