package com.darcy.message.lib_common.app

import android.app.Application

object AppHelper {
    private lateinit var appContext: Application
    fun init(application: Application) {
        appContext = application
    }

    fun getAppContext(): Application {
        return appContext
    }
}