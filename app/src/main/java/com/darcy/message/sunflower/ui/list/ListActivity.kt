package com.darcy.message.sunflower.ui.list

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logI
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
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class ListActivity : BaseActivity<AppActivityListBinding>() {
    private val context: Context by lazy {
        this
    }

    // inject ViewModel
    private val viewModel: ListViewModel by viewModels()

    private val listAdapter = ListAdapter()
    
    // concat the header and footer
    private val fullAdapter =
        listAdapter.withLoadStateHeaderAndFooter(
            header = LoadStateFooterAdapter(retry = {
                logD(message = "retry")
            }),
            footer = LoadStateFooterAdapter(retry = {
                logD(message = "retry")
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
        initView()
        initData()
        initObserver()
    }

    private fun initObserver() {
        binding.btnRefresh.setOnClickListener {
            listAdapter.refresh()
        }
        lifecycleScope.launch {
            listAdapter.loadStateFlow.distinctUntilChanged().collect { loadState ->
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
                when (loadState.append) {
                    is LoadState.Loading -> {
                        logV(message = "append loading")
                    }

                    is LoadState.NotLoading -> {
                        logV(message = "append not loading")
                    }

                    is LoadState.Error -> {
                        logV(message = "append error")
                    }
                }
                when (loadState.prepend) {
                    is LoadState.Loading -> {
                        logI(message = "prepend loading")
                    }

                    is LoadState.NotLoading -> {
                        logI(message = "prepend not loading")
                    }

                    is LoadState.Error -> {
                        logI(message = "prepend error")
                    }
                }

            }
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                load()
            }
        }
    }

    private suspend fun load() {
        logD(message = "Load Data Once")
        viewModel.itemsPaging.collectLatest {
            logD(message = "data-->$it")
            listAdapter.submitData(it)
        }

//        viewModel.itemsPagingNew.collectLatest { it ->
//            logD(message = "data-->$it")
//            listAdapter.submitData(it.map { item ->
//                ListBean().generate(item)
//            })
//        }
    }

    private fun initView() {
        val context = context
        binding.recyclerView.apply {
//            adapter = listAdapter
            adapter = fullAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val decoration =
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
        }
    }
}