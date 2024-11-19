package com.darcy.message.lib_app_status.service.jobscheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import com.darcy.message.lib_app_status.service.task.impl.RandomNumberTask
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logW
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 自定义JobScheduler
 */
class CustomJobSchedulerService : JobService() {
    private val TAG = this::class.java.simpleName
    private val scope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.IO)
    }

    override fun onCreate() {
        super.onCreate()
        logI("$TAG onCreate")
//        val config: Configuration = Configuration.Builder()
//            .setMinimumLoggingLevel(Log.DEBUG)
//            .setJobSchedulerJobIdRange(1000, 2000) // 设置作业 ID 范围从 1000 到 2000
//            .build()
//
//        WorkManager.initialize(this, config)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logI("$TAG onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        logW("$TAG job开始执行 thread:${Thread.currentThread().name}")
        scope.launch {
            val task = RandomNumberTask()
            task.execute(1).also {
                logE("$TAG job执行结果：${it.getOrElse { -1 }}")
            }
        }
        jobFinished(params, true)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        logW("$TAG job执行结束")
        //返回false表示停止后不再重试
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        logE("$TAG onDestroy")
    }
}