package com.darcy.message.sunflower

import android.app.Application
import android.content.res.Configuration
import com.darcy.message.lib_app_status.crash.CustomUncaughtExceptionHandler
import com.darcy.message.lib_app_status.listener.CustomActivityLifecycleListener
import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_common.xlog.XLogHelper
import com.darcy.message.lib_data_store.helper.DataStoreHelper
import com.darcy.message.lib_db.db.DatabaseManager
import com.darcy.message.lib_db.db.impl.ItemRoomDatabase
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // 执行配置改变
        logV(message = "onConfigurationChanged of Application: $newConfig")
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

    private fun startHomeActivity() {
        /** * 跳转到指定应用的首页 */
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(intent)
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
//        DatabaseManager.init(this, ItemRoomDatabase.getDatabase(this))
        DatabaseManager.init(this, ItemRoomDatabase.getCipherDatabase(this))
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