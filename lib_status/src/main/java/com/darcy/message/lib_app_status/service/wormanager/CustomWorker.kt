package com.darcy.message.lib_app_status.service.wormanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * 自定义worker
 */
class CustomWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private val TAG = this::class.java.simpleName

    override fun doWork(): Result {
        logI("$TAG 开始执行任务 thread:${Thread.currentThread().name}")
        repeat(5) {
            TimeUnit.SECONDS.sleep(1)
            logD("$TAG 任务执行中 $it")
        }
        // 执行耗时任务
        val random = Random.Default.nextInt(100)
        logD("随机数 random=$random")
        if (random > 50) {
            logE("$TAG 任务执行失败")
            return Result.failure()
        }
        logD("$TAG 任务执行成功")
        return Result.success()
    }
}