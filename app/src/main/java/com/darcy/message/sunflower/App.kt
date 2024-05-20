package com.darcy.message.sunflower

import android.app.Application
import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_common.xlog.XLogHelper
import com.darcy.message.lib_data_store.helper.DataStoreHelper
import com.darcy.message.lib_db.db.impl.CipherDatabaseHelper
import com.darcy.message.lib_db.db.impl.ItemDatabaseHelper
import com.darcy.message.lib_http.HttpManager
import com.darcy.message.lib_http.client.impl.OKHttpHttpClient
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
        XLogHelper.init(this)
        ItemDatabaseHelper.init(this)
//        CipherDatabaseHelper.init(this)
        DataStoreHelper.init(this)
        HttpManager.init(OKHttpHttpClient)
    }
}