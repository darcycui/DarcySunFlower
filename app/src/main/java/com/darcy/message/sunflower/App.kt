package com.darcy.message.sunflower

import android.app.Application
import com.darcy.message.lib_common.xlog.XLogHelper
import com.darcy.message.lib_db.CipherDatabaseHelper

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        XLogHelper.init(this)
        CipherDatabaseHelper.init(this)
    }
}