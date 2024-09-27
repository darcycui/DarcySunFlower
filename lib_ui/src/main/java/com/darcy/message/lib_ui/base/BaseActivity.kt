package com.darcy.message.lib_ui.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.darcy.message.lib_ui.base.lifecycle.ActivityLifecycle
import com.darcy.message.lib_ui.exts.ViewBindingUtil

abstract class BaseActivity<T : ViewBinding> : ActivityLifecycle() {
    protected val binding: T by lazy {
        ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initListener()
        initData()
    }

    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()
}