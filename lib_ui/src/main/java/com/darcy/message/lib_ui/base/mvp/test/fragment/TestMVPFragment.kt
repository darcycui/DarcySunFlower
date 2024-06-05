package com.darcy.message.lib_ui.base.mvp.test.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.darcy.message.lib_common.exts.toasts
import com.darcy.message.lib_ui.base.mvp.BaseMVPFragment
import com.darcy.message.lib_ui.databinding.LibUiFragmentTestMvpBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TestMVPFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestMVPFragment :
    BaseMVPFragment<LibUiFragmentTestMvpBinding, FragmentContract.FragmentView, FragmentContract.FragmentPresenter>(),
    FragmentContract.FragmentView {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun createPresenter(): FragmentContract.FragmentPresenter {
        return FragmentContract.FragmentPresenter()
    }

    override fun initView() {

    }

    override fun initListener() {
    }

    override fun initData() {
        getPresenter().getCamera()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestMVPFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun showCamera(data: String) {
        binding.textView.text = data
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(msg: String) {
    }

    override fun showEmpty() {
    }

    override fun showContent() {
    }

    override fun showToast(msg: String) {
        requireContext().toasts(msg)
    }
}