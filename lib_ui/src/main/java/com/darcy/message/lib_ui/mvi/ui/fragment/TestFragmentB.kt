package com.darcy.message.lib_ui.mvi.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darcy.message.lib_im.databinding.LibUiFragmentABinding
import com.darcy.message.lib_ui.base.BaseFragment

class TestFragmentB : BaseFragment<LibUiFragmentABinding>() {

    companion object {
        fun newInstance() = TestFragmentB()
    }

    private val viewModel: TestViewModelA by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.message.text = "FragmentB Changed."
    }

}