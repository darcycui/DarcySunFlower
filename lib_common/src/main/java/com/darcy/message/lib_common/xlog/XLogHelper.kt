package com.darcy.message.lib_common.xlog

import android.content.Context
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog

object XLogHelper {
    fun init(context: Context) {
        // darcyRefactor library BuildConfig can not touch ???
//        XLog.init(if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE)
        XLog.init(LogLevel.ALL)
    }
}