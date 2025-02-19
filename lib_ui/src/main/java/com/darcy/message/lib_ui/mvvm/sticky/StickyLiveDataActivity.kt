package com.darcy.message.lib_ui.mvvm.sticky

import android.os.Bundle
import androidx.activity.viewModels
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.lib_ui.databinding.LibUiActivityStickyLiveDataBinding
import com.darcy.message.lib_ui.exts.startPage
import com.darcy.message.lib_ui.mvvm.viewmodel.ActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StickyLiveDataActivity : BaseActivity<LibUiActivityStickyLiveDataBinding>() {
    private val viewModel: ActivityViewModel by viewModels()
    private val scope: CoroutineScope = MainScope()
    private val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {

    }

    override fun initListener() {
        binding.send.setOnClickListener {
            viewModel.addCount()
        }
        binding.observe.setOnClickListener {
            scope.launch {
                delay(1_000)
                viewModel.countLiveData.observe(activity) {
                    binding.tvInfo.text = "count $it"
                }
            }
        }
        binding.goSecondPage.setOnClickListener {
            startPage(SecondActivity::class.java)
        }
    }

    override fun initData() {

    }
}