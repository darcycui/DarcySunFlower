package com.darcy.message.sunflower.ui.notification

import android.content.Intent
import androidx.activity.ComponentActivity
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.lib_ui.exts.startPage
import com.darcy.message.lib_ui.notification.NotificationHelper
import com.darcy.message.lib_ui.notification.ui.parent_activity.HomeActivity
import com.darcy.message.sunflower.databinding.AppActivityNotificationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationActivity : BaseActivity<AppActivityNotificationBinding>() {
    val scope: CoroutineScope = MainScope()
    override fun initView() {

    }

    override fun initListener() {
        binding.run {
            btnShow.setOnClickListener {
                NotificationHelper.show(context as ComponentActivity)
            }
            btnHide.setOnClickListener {
                NotificationHelper.hide(context as ComponentActivity)
            }
            btnShowFullScreen.setOnClickListener {
                scope.launch {
                    delay(3_000)
                    NotificationHelper.showFullScreenNotification(context as ComponentActivity)
                }
            }
            btnHideFullScreen.setOnClickListener {
                NotificationHelper.hideFullScreenNotification(context as ComponentActivity)
            }
            btnForegroundServiceStart.setOnClickListener {
                // 启动前台服务
                startForegroundService(Intent(context, NotificationService::class.java).apply {
                    action = NotificationService.START
                })
            }
            btnForegroundServiceCallIn.setOnClickListener {
                // 启动前台服务
                startForegroundService(Intent(context, NotificationService::class.java).apply {
                    action = NotificationService.CALL_IN
                })
            }
            btnForegroundServiceCallOut.setOnClickListener {
                // 启动前台服务
                startForegroundService(Intent(context, NotificationService::class.java).apply {
                    action = NotificationService.CALL_OUT
                })
            }
            btnForegroundServiceStop.setOnClickListener {
                // 启动前台服务
                startForegroundService(Intent(context, NotificationService::class.java).apply {
                    action = NotificationService.STOP
                })
            }
            btnHome.setOnClickListener {
                startPage(HomeActivity::class.java)
            }

        }

    }

    override fun initData() {

    }
}