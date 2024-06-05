package com.darcy.message.lib_ui.mvi.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logW
import com.darcy.message.lib_ui.databinding.LibUiFragmentABinding
import com.darcy.message.lib_ui.base.BaseFragment

class TestFragmentA : BaseFragment<LibUiFragmentABinding>() {

    companion object {
        fun newInstance() = TestFragmentA().also {
            logW(message = "A newInstance")
            it.arguments = Bundle().apply {
                "PARAM1" to "p1"
                "PARAM2" to "p2"
            }
        }
    }

    private val viewModel: TestViewModelA by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
        arguments?.let {
            val param1 = it.getString("PARAM1")
            logD(message = "param1=$param1")
            val param2 = it.getString("PARAM2")
            logD(message = "param2=$param2")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {
        binding.message.text = "FragmentA Changed."
    }

    override fun initListener() {
    }

    override fun initData() {
    }

}