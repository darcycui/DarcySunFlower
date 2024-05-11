package com.darcy.message.sunflower

import android.os.Bundle
import com.darcy.message.lib_db.db.impl.ItemDatabaseHelper
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.lib_ui.exts.startPage
import com.darcy.message.lib_ui.mvi.ui.MVIActivity
import com.darcy.message.lib_ui.mvi.ui.TestFragmentActivity
import com.darcy.message.lib_ui.paging.ui.TestPagingActivity
import com.darcy.message.sunflower.databinding.AppActivityMainBinding
import com.darcy.message.sunflower.test.DataStoreActivity
import com.darcy.message.sunflower.test.TestHttpActivity
import com.darcy.message.sunflower.ui.detail.DetailActivity
import com.darcy.message.sunflower.ui.list.ListActivity

class MainActivity : BaseActivity<AppActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.btnRoom.setOnClickListener {
            val dbHelper = ItemDatabaseHelper
            dbHelper.init(this.applicationContext)
            dbHelper.testDB(this)
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
    }
}