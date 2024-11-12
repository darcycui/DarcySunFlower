package com.darcy.message.lib_app_status.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.darcy.message.lib_common.exts.logD

class ScreenStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_ON -> {
                logD("屏幕开启")
                // 处理屏幕开启的逻辑
            }
            Intent.ACTION_SCREEN_OFF -> {
                logD("屏幕关闭")
                // 处理屏幕关闭的逻辑
            }
        }
    }
}
