package com.darcy.message.sunflower.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.sunflower.databinding.ActivityDetailBinding
import com.darcy.message.sunflower.ui.detail.adapter.DetailAdapter
import com.darcy.message.sunflower.ui.detail.adapter.LoadStateFooterAdapter
import com.darcy.message.sunflower.ui.detail.bean.IWork
import com.darcy.message.sunflower.ui.detail.bean.Parent
import com.darcy.message.sunflower.ui.detail.bean.Son
import com.darcy.message.sunflower.ui.detail.bean.WorkA
import com.darcy.message.sunflower.ui.detail.di.EverywhereInject
import com.darcy.message.sunflower.ui.detail.di.WorkModule
import com.darcy.message.sunflower.ui.detail.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private val context: Context by lazy {
        this
    }
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    // inject ViewModel
    private val viewModel: DetailViewModel by viewModels()

    private val detailAdapter = DetailAdapter()
    private val fullAdapter =
        detailAdapter.withLoadStateFooter(footer = LoadStateFooterAdapter(retry = {
            logD(message = "retry")
        }))

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
        logV(message = "work=${workA.work()}")
        logV(message = "work=${workB.work()}")
        EverywhereInject().testParentInject(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_detail)
        setContentView(binding.root)
        viewModel.logD(message = "DetailActivity on Create")
        testInject()
        initView()
        initData()
        initObserver()
    }

    private fun initObserver() {
        binding.btnRefresh.setOnClickListener {
            lifecycleScope.launch {
                load()
            }
        }
        binding.btnAppend.setOnClickListener {
            detailAdapter.refresh()
        }
        binding.btnPrepend.setOnClickListener {
            detailAdapter.refresh()
        }
        detailAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    logD(message = "is NotLoading")
                }

                is LoadState.Loading -> {
                    logD(message = "is Loading")
                }

                is LoadState.Error -> {
                    logD(message = "is Error")
                    when ((it.refresh as LoadState.Error).error) {
                        is IOException -> {
                            logD(message = "IOException")
                        }

                        is RuntimeException -> {
                            logD(message = "RuntimeException")
                        }

                        else -> {
                            logD(message = "others exception")
                        }
                    }
                }
            }
//            when (it.append) {
//
//            }
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
            detailAdapter.submitData(it)
        }
    }

    private fun initView() {
        val context = context
        binding.recyclerView.apply {
//            adapter = detailAdapter
            adapter = fullAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val decoration =
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
        }
    }
}