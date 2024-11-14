package com.darcy.message.lib_app_status.service

import android.app.job.JobParameters
import android.app.job.JobService
import com.darcy.message.lib_common.exts.logW

class CustomJobScheduler: JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        logW("CustomJobScheduler job开始执行")
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        logW("CustomJobScheduler job执行结束")
        return false
    }
}