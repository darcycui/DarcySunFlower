package com.darcy.message.lib_camera.camera1.service

import android.app.AlarmManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.darcy.message.lib_camera.camera1.activity.InVisibleCameraActivity
import com.darcy.message.lib_common.exts.logD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MessageService : Service() {
    private val context: Context by lazy {
        this
//        TestCameraBackgroundActivity.getContext()
    }
    private val mainScope: CoroutineScope by lazy {
        MainScope()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        logD("MessageService onCreate")
    }

    private fun initMessageSocket() {
        mainScope.launch {
            repeat(3) { count ->
                logD("MessageService initMessageSocket: delay $count start")
                repeat(5) { second ->
                    logD("delay second:$second")
                    delay(1_000)
                }
                logD("MessageService initMessageSocket: delay $count end")
                doCapture()
            }
        }
    }

    private fun doCapture() {
        startActivity(Intent(context, InVisibleCameraActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        })
//        startService(Intent(context, BackgroundCameraService::class.java))
//        startService(Intent(context, WindowManagerCameraService::class.java))
//        startForegroundService(Intent(context, ForegroundCameraService::class.java))
        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
////            val pendingIntent = PendingIntent.getForegroundService(
////                context,
////                0,
////                Intent(context, ForegroundCameraService::class.java),
////                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
////            )
//            val pendingIntent = PendingIntent.getActivity(
//                context,
//                0,
//                Intent(context, MiddleActivity::class.java).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                },
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//
//            logI("Starting camera service with exact alarm")
//            // 10秒后触发
//            val triggerAtMillis = System.currentTimeMillis() + 0 * 1000L
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    triggerAtMillis,
//                    pendingIntent
//                )
//            } else {
//
//            }
//        } else {
//            ContextCompat.startForegroundService(context, Intent(context, ForegroundCameraService::class.java))
//        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initMessageSocket()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        println("MessageService onDestroy")
    }
}