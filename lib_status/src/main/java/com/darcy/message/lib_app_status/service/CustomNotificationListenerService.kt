package com.darcy.message.lib_app_status.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.darcy.message.lib_common.exts.logD


/**
 * 监听通知栏的服务,需要用户授权
 */
class CustomNotificationListenerService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val tickerText = sbn.notification.tickerText
        val notificationId = sbn.id

        logD("通知发布: 包名=$packageName, 内容=$tickerText, ID=$notificationId")
        // 处理通知发布的逻辑
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val tickerText = sbn.notification.tickerText
        val notificationId = sbn.id

        logD("通知移除: 包名=$packageName, 内容=$tickerText, ID=$notificationId")
        // 处理通知移除的逻辑
    }
}
