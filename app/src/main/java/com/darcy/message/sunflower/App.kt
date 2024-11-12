package com.darcy.message.sunflower

import android.app.Application
import com.darcy.message.lib_app_status.crash.CustomUncaughtExceptionHandler
import com.darcy.message.lib_app_status.listener.CustomActivityLifecycleListener
import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_common.xlog.XLogHelper
import com.darcy.message.lib_data_store.helper.DataStoreHelper
import com.darcy.message.lib_db.db.DatabaseManager
import com.darcy.message.lib_db.db.impl.ItemRoomDatabase
import com.darcy.message.lib_http.BuildConfig
import com.darcy.message.lib_http.HttpManager
import com.darcy.message.lib_http.client.impl.OKHttpHttpClient
import com.darcy.message.lib_umeng.ReportManager
import com.darcy.message.sunflower.ui.list.bean.Parent
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var parent: Parent

    override fun onCreate() {
        super.onCreate()
        init()
        logV(message = "parent=$parent")
    }

    private fun init() {
        AppHelper.init(this)
        XLogHelper.init(this, BuildConfig.DEBUG)
        initDatabase()
        DataStoreHelper.init(this)
        initHttp()
        initReport()
        initAppStatus()
    }

    private fun initAppStatus() {
        registerActivityLifecycleCallbacks(CustomActivityLifecycleListener)
        Thread.setDefaultUncaughtExceptionHandler(CustomUncaughtExceptionHandler)
    }

    // Report
    private fun initReport() {
        ReportManager.preInit()
        ReportManager.init(BuildConfig.DEBUG)
    }

    // DB
    private fun initDatabase() {
        DatabaseManager.init(this, ItemRoomDatabase.getDatabase(this))
//        CipherDatabaseHelper.init(this)
    }


    // Http
    private fun initHttp() {
        HttpManager.init(OKHttpHttpClient)
//        HttpManager.init(RetrofitHttpClient)
//        HttpManager.init(KtorHttpClient)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logE("onLowMemory of Application: delete temp files...")
    }
}