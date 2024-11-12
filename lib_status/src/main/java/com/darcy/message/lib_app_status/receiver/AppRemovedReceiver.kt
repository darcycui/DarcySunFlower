package com.darcy.message.lib_app_status.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.darcy.message.lib_common.exts.logE


class AppRemovedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_SHUTDOWN == intent.action ||
            Intent.ACTION_PACKAGE_REMOVED == intent.action
        ) {
            logE("AppKilledReceiver: $intent")
        }
    }
}