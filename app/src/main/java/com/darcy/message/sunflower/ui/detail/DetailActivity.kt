package com.darcy.message.sunflower.ui.detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_ui.paging.Injection
import com.darcy.message.lib_ui.paging.viewmodel.ArticleViewModel
import com.darcy.message.sunflower.R
import com.darcy.message.sunflower.databinding.ActivityDetailBinding
import com.darcy.message.sunflower.databinding.ActivityMainBinding
import com.darcy.message.sunflower.ui.detail.adapter.DetailAdapter
import com.darcy.message.sunflower.ui.detail.viewmodel.DetailViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private val context: Context by lazy {
        this
    }
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<DetailViewModel>(
        factoryProducer = { Injection.provideViewModelFactory(owner = this) }
    )
    private val detailAdapter = DetailAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_detail)
        setContentView(binding.root)
        initView()
        initData()
    }

    private fun initData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                logD(message = "Load Data Once")
                viewModel.itemsPaging.collectLatest {
                    detailAdapter.submitData(it)
                }
            }
        }
    }

    private fun initView() {
        val context = context
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val decoration =
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            adapter = detailAdapter
        }
    }
}