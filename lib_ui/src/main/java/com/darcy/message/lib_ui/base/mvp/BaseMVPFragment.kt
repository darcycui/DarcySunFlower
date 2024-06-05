package com.darcy.message.lib_ui.base.mvp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.darcy.message.lib_ui.base.BaseFragment

abstract class BaseMVPFragment<VB : ViewBinding, V : IView, P : IPresenter<V>> :
    BaseFragment<VB>() {
    private val _presenter: P by lazy { createPresenter() }
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPresenter().onCreate()
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

    override fun onDestroyView() {
        super.onDestroyView()
        getPresenter().onDetachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        getPresenter().onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    protected fun getPresenter(): P {
        return _presenter
    }

    abstract fun createPresenter(): P
}