package com.darcy.message.lib_ui.paging.ui

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.darcy.message.lib_ui.databinding.LibUiActivityArticlesBinding
import com.darcy.message.lib_ui.paging.viewmodel.ArticleViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.lib_ui.paging.Injection
import com.darcy.message.lib_ui.paging.adapter.LoadStateFooterAdapter
import com.darcy.message.lib_ui.paging.adapter.MultipleTypeAdapter

class TestPagingActivity : BaseActivity<LibUiActivityArticlesBinding>() {

    private val viewModel by viewModels<ArticleViewModel>(
        factoryProducer = { Injection.provideViewModelFactory(owner = this) }
    )

//    private val articleAdapter = ArticleAdapter()
    private val adapter = MultipleTypeAdapter()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bindAdapter(adapter = adapter)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.itemsPaging.collectLatest {
                    // 通知adapter数据变化
                    adapter.submitData(it)
                }
            }
        }
        lifecycleScope.launch {

            adapter.loadStateFlow.collect { loadStates ->
                loadStates.apply {
                    logV(message = "loadStates.refresh=${loadStates.refresh}")
                }
                binding.loading.isVisible = loadStates.refresh is LoadState.Loading
                binding.retry.isVisible = loadStates.refresh is LoadState.Error
                binding.rvList.isVisible = loadStates.refresh is LoadState.NotLoading
            }
        }

        binding.btnRefresh.setOnClickListener {
            adapter.refresh()
        }

        binding.retry.setOnClickListener {
            adapter.retry()
        }
    }
}

private fun LibUiActivityArticlesBinding.bindAdapter(
    adapter: PagingDataAdapter<*, *>
) {
    rvList.adapter =
        adapter.withLoadStateHeaderAndFooter(
            header = LoadStateFooterAdapter { adapter.retry() },
            footer = LoadStateFooterAdapter { adapter.retry() }
        )
    rvList.layoutManager = LinearLayoutManager(rvList.context)
    // add divider by itemDecoration
    val decoration = DividerItemDecoration(rvList.context, DividerItemDecoration.VERTICAL)
    rvList.addItemDecoration(decoration)
}