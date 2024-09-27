package com.darcy.message.sunflower.ui.list

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.sunflower.databinding.AppActivityListBinding
import com.darcy.message.sunflower.ui.list.adapter.ListAdapter
import com.darcy.message.sunflower.ui.list.adapter.LoadStateFooterAdapter
import com.darcy.message.sunflower.ui.list.bean.IWork
import com.darcy.message.sunflower.ui.list.bean.ListBean
import com.darcy.message.sunflower.ui.list.bean.Parent
import com.darcy.message.sunflower.ui.list.bean.Son
import com.darcy.message.sunflower.ui.list.di.EverywhereInject
import com.darcy.message.sunflower.ui.list.di.WorkModule
import com.darcy.message.sunflower.ui.list.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListActivity : BaseActivity<AppActivityListBinding>() {

    // inject ViewModel
    private val viewModel: ListViewModel by viewModels()

    private val adapter = ListAdapter()

    // concat the header and footer
    private val fullAdapter =
        adapter.withLoadStateHeaderAndFooter(
            header = LoadStateFooterAdapter(retry = {
                logD(message = "header retry")
                adapter.retry()
            }),
            footer = LoadStateFooterAdapter(retry = {
                logD(message = "footer retry")
                adapter.retry()
            })
        )

    @Inject
    lateinit var son: Son

    @Inject
    lateinit var parent: Parent

    @Inject
    @WorkModule.A
    lateinit var workA: IWork

    @Inject
    @WorkModule.B
    lateinit var workB: IWork

    private fun testInject() {
        logV(message = "son=$son")
        logV(message = "parent=$parent")
        workA.work()
        workB.work()
        EverywhereInject().testParentInject(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.logD(message = "DetailActivity on Create")
        testInject()
        initListener()
    }

    private fun initObservers() {
        binding.btnRefresh.setOnClickListener {
            adapter.refresh()
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.distinctUntilChanged().collect { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        logD(message = "refresh loading")
                    }

                    is LoadState.NotLoading -> {
                        logD(message = "refresh not loading")
                    }

                    is LoadState.Error -> {
                        logD(message = "refresh error")
                    }
                }
                binding.loading.isVisible = loadState.refresh is LoadState.Loading
                binding.retry.isVisible = loadState.refresh is LoadState.Error
                binding.recyclerView.isVisible = loadState.refresh is LoadState.NotLoading
            }
        }
    }

    override fun initData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                load()
            }
        }
    }

    private suspend fun load() {
        logD(message = "Load Data Once")
        // 1.from custom PageSource
//        viewModel.itemsPaging.collectLatest { pagingData->
//            logD(message = "data-->$pagingData")
//            adapter.submitData(pagingData)
//        }

        // 2.from Room PageSource
//        viewModel.itemsPagingFromRoom.collectLatest { pagingData ->
//            logD(message = "data from room-->$pagingData")
//            adapter.submitData(pagingData)
//        }

        // 3.from RemoteMediator PageSource // darcyRefactor tobe continued
        viewModel.itemsPagingFromRoomAndHttp.collectLatest { pagingData ->
            logE(message = "data from room and http-->${pagingData}")
            adapter.submitData(pagingData)
        }
    }

    override fun initView() {
        val context = context
        binding.recyclerView.apply {
//            adapter = listAdapter
            adapter = fullAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            // add divider by itemDecoration
            val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
        }

        binding.retry.setOnClickListener {
            adapter.retry()
        }
    }

    override fun initListener() {
        initObservers()
    }
}