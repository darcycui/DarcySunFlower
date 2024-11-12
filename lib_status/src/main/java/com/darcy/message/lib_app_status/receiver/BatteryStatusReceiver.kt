package com.darcy.message.lib_app_status.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.darcy.message.lib_common.exts.logD

class BatteryStatusReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL

            if (isCharging) {
                logD("BatteryStatus:设备正在充电")
                // 处理设备充电的逻辑
            } else {
                logD("BatteryStatus:设备已断开充电")
                // 处理设备断开充电的逻辑
            }
        }
    }
}
