package com.darcy.message.lib_ui.notification

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.toasts
import com.darcy.message.lib_permission.permission.PermissionUtil
import com.darcy.message.lib_permission.permission.toAppSettingsDetail


object NotificationUtil {
    fun show(context: ComponentActivity) {
        println("NotificationUtil.show")
        checkNotificationPermission(context,
            onGranted = {
                context.toasts("has Permission")
            }, onDenied = {
                context.toasts("has no Permission")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PermissionUtil.requestPermissions(context,
                        listOf(Manifest.permission.POST_NOTIFICATIONS),
                        onGranted = {
                            context.toasts("Permission granted")
                        }, onDenied = {shouldShowRequestPermissionRationale->
                            context.toasts("Permission denied")
                            if (shouldShowRequestPermissionRationale) {
                                context.toasts("Please open notification settings")
                                context.toAppSettingsDetail(102)
                            }
                        })
                } else {
                    context.toAppSettingsDetail(102)
                }
            })
    }

    fun hide(context: Context) {
        println("NotificationUtil.hide")
    }

    fun isShowing(): Boolean {
        return false
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

}