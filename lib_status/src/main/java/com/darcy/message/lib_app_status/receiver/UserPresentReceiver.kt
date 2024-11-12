package com.darcy.message.lib_app_status.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.darcy.message.lib_common.exts.logI

class UserPresentReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_USER_PRESENT) {
            logI("receiver:user present")
        }
    }
}