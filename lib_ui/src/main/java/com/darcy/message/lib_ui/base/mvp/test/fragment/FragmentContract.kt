package com.darcy.message.lib_ui.base.mvp.test.fragment

import com.darcy.message.lib_ui.base.mvp.BasePresenter
import com.darcy.message.lib_ui.base.mvp.IView

interface FragmentContract {
    interface FragmentView : IView {
        fun showCamera(data: String)
    }

    class FragmentPresenter : BasePresenter<FragmentView>() {
        fun getCamera() {
            val data = "Camera Load Success"
            ui {
                getView()?.showCamera(data)
            }
        }
    }
}