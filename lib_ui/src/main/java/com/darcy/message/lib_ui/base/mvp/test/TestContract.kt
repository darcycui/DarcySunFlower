package com.darcy.message.lib_ui.base.mvp.test

import com.darcy.message.lib_ui.base.mvp.BasePresenter
import com.darcy.message.lib_ui.base.mvp.IView

interface TestContract {
    interface TestView : IView {
        fun showHello(data: String)
    }

    class TestPresenter : BasePresenter<TestView>() {
        fun getHello() {
            val data = "Hello World"
            ui {
                getView()?.showHello(data)
            }
        }
    }
}