package com.darcy.message.lib_ui.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.darcy.message.lib_ui.base.lifecycle.ActivityLifecycle
import com.darcy.message.lib_ui.exts.ViewBindingUtil

abstract class BaseActivity<T> : ActivityLifecycle() where T : ViewBinding {
    protected val binding: T by lazy {
        ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        intListener()
        initData()
    }

    abstract fun initView()
    abstract fun intListener()
    abstract fun initData()
}