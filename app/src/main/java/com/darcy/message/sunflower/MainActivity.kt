package com.darcy.message.sunflower

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.darcy.message.lib_db.db.impl.ItemDatabaseHelper
import com.darcy.message.lib_ui.exts.startPage
import com.darcy.message.lib_ui.paging.ui.ArticleActivity
import com.darcy.message.sunflower.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.btnRoom.setOnClickListener {
            val dbHelper = ItemDatabaseHelper
            dbHelper.init(this.applicationContext)
            dbHelper.testDB(this)
        }
        binding.btnPaging.setOnClickListener {
            startPage(ArticleActivity::class.java)
        }
    }
}