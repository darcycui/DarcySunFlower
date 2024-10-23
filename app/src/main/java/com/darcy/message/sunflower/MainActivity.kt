package com.darcy.message.sunflower

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.darcy.message.lib_common.arraymap.TestArrayMap
import com.darcy.message.lib_common.arraymap.TestArraySet
import com.darcy.message.lib_common.sparsearray.TestSparseArray
import com.darcy.message.lib_common.sparsearray.TestSparseBooleanArray
import com.darcy.message.lib_common.sparsearray.TestSparseIntArray
import com.darcy.message.lib_common.sparsearray.TestSparseLongArray
import com.darcy.message.lib_db.db.DatabaseManager
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.lib_ui.base.mvp.test.TestMVPActivity
import com.darcy.message.lib_ui.exts.startPage
import com.darcy.message.lib_ui.mvi.ui.MVIActivity
import com.darcy.message.lib_ui.mvi.ui.TestFragmentActivity
import com.darcy.message.lib_ui.paging.ui.TestPagingActivity
import com.darcy.message.sunflower.databinding.AppActivityMainBinding
import com.darcy.message.sunflower.lib_security.TestSecurityActivity
import com.darcy.message.sunflower.test.DataStoreActivity
import com.darcy.message.sunflower.test.RoomTestActivity
import com.darcy.message.sunflower.test.TestHttpActivity
import com.darcy.message.sunflower.test.TestJniActivity
import com.darcy.message.sunflower.ui.detail.DetailActivity
import com.darcy.message.sunflower.ui.list.ListActivity
import com.darcy.message.sunflower.ui.notification.NotificationActivity
import com.darcy.message.sunflower.ui.settings.SettingsActivity

class MainActivity : BaseActivity<AppActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // follow system dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    override fun initView() {
        binding.btnRoom.setOnClickListener {
            startPage(RoomTestActivity::class.java)
        }
        binding.btnRoomFlow.setOnClickListener {
            startPage(DetailActivity::class.java)
        }
        binding.btnPaging.setOnClickListener {
            startPage(TestPagingActivity::class.java)
        }
        binding.btnRoomPaging.setOnClickListener {
            startPage(ListActivity::class.java)
        }
        binding.btnMVI.setOnClickListener {
            startPage(MVIActivity::class.java)
        }
        binding.btnDataStore.setOnClickListener {
            startPage(DataStoreActivity::class.java)
        }
        binding.btnFragment.setOnClickListener {
            startPage(TestFragmentActivity::class.java)
        }
        binding.btnHttp.setOnClickListener {
            startPage(TestHttpActivity::class.java)
        }
        binding.btnMvp.setOnClickListener {
            startPage(TestMVPActivity::class.java)
        }
        binding.btnSparseArray.setOnClickListener {
            TestSparseArray.test()
            TestSparseBooleanArray.test()
            TestSparseIntArray.test()
            TestSparseLongArray.test()
            TestArrayMap.test()
            TestArraySet.test()
        }
        binding.btnNotification.setOnClickListener {
            startPage(NotificationActivity::class.java)
        }
        binding.btnJni.setOnClickListener {
            startPage(TestJniActivity::class.java)
        }
        binding.btnSecurity.setOnClickListener {
            startPage(TestSecurityActivity::class.java)
        }
    }

    override fun initListener() {

    }

    override fun initData() {

    }
}