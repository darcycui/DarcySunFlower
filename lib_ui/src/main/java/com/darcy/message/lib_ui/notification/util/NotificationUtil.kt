package com.darcy.message.lib_ui.notification.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_permission.permission.PermissionUtil

object NotificationUtil {

    fun checkFullScreenNotificationPermission(
        context: Context,
        onGranted: () -> Unit,
        onDenied: () -> Unit,
    ) {
        val result = NotificationManagerCompat.from(context)
            .canUseFullScreenIntent()
        if (result) {
            onGranted.invoke()
        } else {
            onDenied.invoke()
        }
    }


    fun checkNotificationPermission(
        context: Context,
        onGranted: () -> Unit,
        onDenied: () -> Unit,
    ) {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? android.app.NotificationManager?
                ?: return
        if (manager.areNotificationsEnabled() && PermissionUtil.checkPermissions(
                context,
                listOf(Manifest.permission.POST_NOTIFICATIONS)
            )
        ) {
            logD("checkNotificationPermission: granted")
            onGranted()
        } else {
            logE("checkNotificationPermission: not granted")
            onDenied()
        }
    }

    fun openSettings(context: Context, actionName: String) {
        Intent(actionName, Uri.parse("package:" + context.packageName)).apply {
            context.startActivity(this)
        }
    }
}