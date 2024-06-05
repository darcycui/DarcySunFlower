package com.darcy.message.lib_ui.base.mvp.test

import android.os.Bundle
import com.darcy.message.lib_common.exts.toast
import com.darcy.message.lib_ui.R
import com.darcy.message.lib_ui.base.mvp.BaseMVPActivity
import com.darcy.message.lib_ui.base.mvp.test.fragment.TestMVPFragment
import com.darcy.message.lib_ui.databinding.LibUiActivityTestMvpBinding

class TestMVPActivity :
    BaseMVPActivity<LibUiActivityTestMvpBinding, TestContract.TestView, TestContract.TestPresenter>(),
    TestContract.TestView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun createPresenter(): TestContract.TestPresenter {
        return TestContract.TestPresenter()
    }

    override fun initView() {
    }

    override fun intListener() {
    }

    override fun initData() {
        getPresenter().getHello()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, TestMVPFragment.newInstance("", "")).commit()
    }

    override fun showHello(data: String) {
        binding.tvName.text = data
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
        toast(msg)
    }
}