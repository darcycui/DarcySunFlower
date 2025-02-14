package com.darcy.message.lib_app_status.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.darcy.message.lib_common.exts.logE


class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        logE("BootCompletedReceiver 开机自启动")
        if (intent.action == ACTION_BOOT_COMPLETED) {
            Toast.makeText(context, "开机自启动", Toast.LENGTH_SHORT).show()
//            val intentMain = Intent(context, TestAppStatusActivity::class.java)
//            intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(intentMain)
        }
    }

    companion object {
        private const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }
}