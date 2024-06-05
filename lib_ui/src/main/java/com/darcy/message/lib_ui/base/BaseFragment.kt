package com.darcy.message.lib_ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.darcy.message.lib_ui.base.lifecycle.FragmentLifecycle
import com.darcy.message.lib_ui.exts.ViewBindingUtil

abstract class BaseFragment<T> : FragmentLifecycle() where T : ViewBinding {

    private var _binding: T? = null

    protected val binding : T get() = requireNotNull(_binding) {
        "ViewBinding is null"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = container?.let { ViewBindingUtil.inflateWithGeneric(this, it) }
        return _binding?.root ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()
}