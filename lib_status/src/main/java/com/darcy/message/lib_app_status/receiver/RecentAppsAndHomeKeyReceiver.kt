package com.darcy.message.lib_app_status.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.darcy.message.lib_common.exts.logE


private const val CLOSE_SYSTEM_DIALOG_ACTION: String = "reason"
private const val RECENT_APPS_KEY: String = "recentapps"
private const val HOME_KEY: String = "homekey"

class RecentAppsAndHomeKeyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS == action) {
            val reason = intent.getStringExtra(CLOSE_SYSTEM_DIALOG_ACTION)

            if (reason != null) {
                if (reason == HOME_KEY) {
                    // "Home键被监听"
                    logE("receiver:home key")
                } else if (reason == RECENT_APPS_KEY) {
                    //"多任务键被监听"
                    logE("receiver:rencent apps key")
                }
            }
        }
    }
}