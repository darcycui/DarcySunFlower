package com.darcy.message.lib_ui.mvvm.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_ui.base.BaseFragment
import com.darcy.message.lib_ui.databinding.LibUiFragmentABinding
import com.darcy.message.lib_ui.mvvm.viewmodel.ActivityViewModel

class TestFragmentB : BaseFragment<LibUiFragmentABinding>() {

    companion object {
        fun newInstance() = TestFragmentB()
    }

    // 使用activity的viewModel
    private val viewModel: ActivityViewModel by viewModels({ requireActivity() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {
        binding.message.text = "FragmentB Changed."
    }

    override fun initListener() {
        viewModel.countLiveData.observe(this) {
            logI(message = "BBB countLiveData=$it")
            binding.message.text = "count=$it"
        }
    }

    override fun initData() {
    }

}