package com.darcy.message.lib_task.engine.threadpool

import java.util.concurrent.ExecutorService

interface IThreadPool {
    fun getExecutor(): ExecutorService
}