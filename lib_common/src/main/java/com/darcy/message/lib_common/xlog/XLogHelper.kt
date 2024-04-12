package com.darcy.message.lib_common.xlog

import android.content.Context
import com.darcy.message.lib_common.BuildConfig
import com.darcy.message.lib_common.exts.LOG_TAG
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog

object XLogHelper {
    fun init(context: Context) {
        // darcyRefactor library BuildConfig can not touch need change build.gradle.
        val level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
        val config = LogConfiguration.Builder()
            .logLevel(level)
            .tag(LOG_TAG)
//      .enableThreadInfo()
//      .enableBorder()
//      .enableStackTrace(1)
            .build()
        XLog.init(config)
    }
}