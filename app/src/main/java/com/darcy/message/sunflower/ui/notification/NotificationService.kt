package com.darcy.message.sunflower.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.darcy.message.lib_brand.BrandHelper
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_ui.R
import com.darcy.message.lib_ui.notification.ui.FullScreenNotificationActivity


class NotificationService : Service() {
    private val NOTIFICATION_ID: Int = 1
    private val context = this
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 创建通知渠道（适用于 Android 8.0 及以上）
        createNotificationChannel()

        val action = intent?.action ?: "执行任务中..."
        // 创建通知
        val builder = NotificationCompat.Builder(this, "my_channel_id")
            .setContentTitle("前台服务正在运行")
            .setContentText(action)
            .setSmallIcon(R.drawable.lib_ui_notify)
        if (action == CALL_IN) {
            // 创建点击通知后打开的 Activity 的 Intent
            val intentClick = Intent(context, FullScreenNotificationActivity::class.java).apply {
                putExtra("title", "全屏通知标题")
                putExtra("message", "全屏通知内容")
            }
            val fullScreenPendingIntent: PendingIntent =
                PendingIntent.getActivity(context, 1, intentClick, PendingIntent.FLAG_IMMUTABLE)
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
        }


        // 将服务设置为前台服务
        startForeground(NOTIFICATION_ID, builder.build())
        return START_STICKY
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            "my_channel_id",
            "前台服务通道",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    override fun onDestroy() {

    }

    companion object {
        val START: String = "start"
        val CALL_IN: String = "call_in"
        val CALL_OUT: String = "call_out"
        val STOP: String = "stop"
    }
}