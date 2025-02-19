package com.darcy.message.lib_ui.mvvm.sticky

import android.os.Bundle
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.lib_ui.databinding.LibUiActivitySecondBinding
import com.darcy.message.lib_ui.mvvm.viewmodel.ActivityViewModel

class SecondActivity : BaseActivity<LibUiActivitySecondBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {

    }

    override fun initListener() {
        ActivityViewModel.getGlobalLiveDataForStickyTest().observe(this) {
            binding.tvInfo.text = "sticky value: $it"
        }
    }

    override fun initData() {
    }
}