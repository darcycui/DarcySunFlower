package com.darcy.lib_download.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.darcy.lib_download.R
import com.darcy.lib_download.databinding.LibDownloadActivityTestDownloadMachineListBinding
import com.darcy.lib_download.ui.adapter.AppAdapter
import com.darcy.lib_download.ui.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestDownloadMachineListActivity : AppCompatActivity() {
    private val binding: LibDownloadActivityTestDownloadMachineListBinding by lazy {
        LibDownloadActivityTestDownloadMachineListBinding.inflate(layoutInflater)
    }
    private val context: Context by lazy { this }
    private val appAdapter: AppAdapter by lazy { AppAdapter() }
    private val repository: AppRepository by lazy { AppRepository() }
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
        initObserver()
        initData()
    }

    private fun initView() {
        binding.recyclerView.apply {
            adapter = appAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            // 去除item更新动画
            itemAnimator = null
        }
    }

    private fun initObserver() {
    }

    private fun initData() {
        scope.launch {
            val list = repository.getAppList()
            scope.launch(Dispatchers.Main) {
                appAdapter.setData(list)
            }
        }
    }
}