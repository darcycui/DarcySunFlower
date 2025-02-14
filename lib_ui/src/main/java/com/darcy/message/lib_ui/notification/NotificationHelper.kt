package com.darcy.message.lib_ui.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.darcy.message.lib_brand.BrandHelper
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.toasts
import com.darcy.message.lib_permission.permission.PermissionUtil
import com.darcy.message.lib_permission.permission.toAppSettingsDetail
import com.darcy.message.lib_ui.R
import com.darcy.message.lib_ui.notification.ui.FullScreenNotificationActivity
import com.darcy.message.lib_ui.notification.ui.TargetActivity
import com.darcy.message.lib_ui.notification.util.NotificationUtil


object NotificationHelper {
    fun show(
        context: ComponentActivity,
        notification: (context: Context) -> Unit = ::sendNotification
    ) {
        println("NotificationUtil.show")
        NotificationUtil.checkNotificationPermission(context,
            onGranted = {
                context.toasts("has Permission")
                notification.invoke(context)
            }, onDenied = {
                context.toasts("has no Permission")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PermissionUtil.requestPermissions(context,
                        listOf(Manifest.permission.POST_NOTIFICATIONS),
                        onGranted = {
                            context.toasts("Permission granted")
                            notification.invoke(context)
                        }, onDenied = { shouldShowRequestPermissionRationale ->
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


    fun update(context: Context) {
        println("Notification.update")
    }

    fun hide(context: Context) {
        println("Notification.hide")
    }

    fun isShowing(): Boolean {
        return false
    }

    fun showFullScreenNotification(
        context: Context,
        notification: (context: Context) -> Unit = ::sendFullScreenNotification
    ) {
//        NotificationUtil.openSettings(context, ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT)
        NotificationUtil.checkFullScreenNotificationPermission(context,
            onGranted = {
                context.toasts("FullScreenNotification granted")
                notification.invoke(context)
            },
            onDenied = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    context.toasts("FullScreenNotification denied")
                    NotificationUtil.openSettings(context, ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT)
                } else {
                    context.toasts("FullScreenNotification granted by default")
                    notification.invoke(context)
                }
            })
    }

    fun hideFullScreenNotification(context: Context) {

    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(context: Context) {
        // 创建通知渠道
        val name = "Channel Name"
        val descriptionText = "Channel Description"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // 创建回退栈
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
        // 设置要回退的页面
        stackBuilder.addParentStack(TargetActivity::class.java)
        // 创建点击通知后打开的页面
        stackBuilder.addNextIntent(Intent(context, TargetActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })

        val pendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)

        // 构建通知
        val builder = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.lib_ui_notify)
            .setContentTitle("通知标题")
            .setContentText("通知内容")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // 发送通知
        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendFullScreenNotification(context: Context) {
        // 创建通知渠道
        val name = "Channel Name2"
        val descriptionText = "Channel Description2"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("channel_id2", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // 创建点击通知后打开的 Activity 的 Intent
        val intent = Intent(context, FullScreenNotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("title", "全屏通知标题")
            putExtra("message", "全屏通知内容")
        }
        val fullScreenPendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        // 构建通知
        val builder = NotificationCompat.Builder(context, "channel_id2")
            .setSmallIcon(R.drawable.lib_ui_notify)
            .setContentTitle("全屏通知标题")
            .setContentText("全屏通知内容")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setAutoCancel(false)
        // darcyRefactor: 检查是否允许后台启动页面
        if (BrandHelper.isBackgroundStartAllowed(context)) {
            // 设置为全屏通知
            logI("全屏通知允许后台启动")
            builder.setFullScreenIntent(fullScreenPendingIntent, true)
        } else {
            // 设置为普通通知
            logE("全屏通知不允许后台启动!!!")
            builder.setContentIntent(fullScreenPendingIntent)
        }


        // 发送通知
        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }

}