package com.darcy.message.sunflower.ui.notification

import androidx.activity.ComponentActivity
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.lib_ui.notification.NotificationUtil
import com.darcy.message.sunflower.databinding.AppActivityNotificationBinding

class NotificationActivity :BaseActivity<AppActivityNotificationBinding>() {
    override fun initView() {

    }

    override fun initListener() {
        binding.run {
            btnShow.setOnClickListener {
                NotificationUtil.show(context as ComponentActivity)
            }
            btnHide.setOnClickListener {
                NotificationUtil.hide(context as ComponentActivity)
            }

        }

    }

    override fun initData() {

    }
}