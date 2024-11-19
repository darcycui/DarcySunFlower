package com.darcy.message.lib_app_status.service.intentservice

import android.app.IntentService
import android.content.Intent
import com.darcy.message.lib_app_status.service.task.impl.RandomNumberTask
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI

class CustomIntentService : IntentService("CustomIntentService") {
    private val TAG = this::class.java.simpleName
    var count = 0
    override fun onHandleIntent(intent: Intent?) {
        logI("$TAG 开始执行任务 thread:${Thread.currentThread().name}")
        val task = RandomNumberTask()
        task.execute(count)
        logE("$TAG 任务执行完毕 thread:${Thread.currentThread().name}")
    }

    override fun onCreate() {
        super.onCreate()
        logD("$TAG onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        count++
        logD("$TAG onStartCommand count=$count")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        logE("$TAG onDestroy")
    }
}