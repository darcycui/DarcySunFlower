package com.darcy.message.lib_app_status.service.task.impl

import com.darcy.message.lib_app_status.service.task.ITask
import com.darcy.message.lib_common.exts.logD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class RandomNumberTask : ITask<Int> {
    private val TAG = this::class.java.simpleName

    private val scope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.IO)
    }

    override fun execute(count: Int): Result<Int> {
        logD("count=$count 开始执行任务 thread=${Thread.currentThread().name}")
        repeat(30) {
            TimeUnit.SECONDS.sleep(1)
            logD("count=$count 任务执行中 $it")
        }
        // 执行耗时任务
        val random = Random.Default.nextInt(100)
        logD("count=$count 随机数 random=$random")
        if (random > 50) {
            return Result.success(random)
        }
        return Result.success(random)
    }
}