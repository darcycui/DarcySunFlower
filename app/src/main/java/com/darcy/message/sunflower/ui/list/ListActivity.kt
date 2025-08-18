package com.darcy.message.sunflower.ui.list

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.sunflower.databinding.AppActivityListBinding
import com.darcy.message.sunflower.ui.list.adapter.ReposAdapter
import com.darcy.message.sunflower.ui.list.adapter.ReposLoadStateAdapter
import com.darcy.message.sunflower.ui.list.bean.IWork
import com.darcy.message.sunflower.ui.list.bean.Parent
import com.darcy.message.sunflower.ui.list.bean.Son
import com.darcy.message.sunflower.ui.list.bean.UiModel
import com.darcy.message.sunflower.ui.list.di.EverywhereInject
import com.darcy.message.sunflower.ui.list.di.WorkModule
import com.darcy.message.sunflower.ui.list.viewmodel.ListViewModel
import com.darcy.message.sunflower.ui.list.viewmodel.UiAction
import com.darcy.message.sunflower.ui.list.viewmodel.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListActivity : BaseActivity<AppActivityListBinding>() {

    // inject ViewModel
    private val viewModel: ListViewModel by viewModels()

    private val adapter = ReposAdapter()

    // concat the header and footer
    private val fullAdapter =
        adapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter(retry = {
                logD(message = "header retry")
                adapter.retry()
            }),
            footer = ReposLoadStateAdapter(retry = {
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
        // bind the state
        binding.bindState(
            uiState = viewModel.uiState,
            pagingData = viewModel.pagingDataFlow,
            uiActions = viewModel.uiAction
        )
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
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    private fun AppActivityListBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<UiModel>>,
        uiActions: (UiAction) -> Unit
    ) {
        val repoAdapter = ReposAdapter()
        val header = ReposLoadStateAdapter { repoAdapter.retry() }
        // 设置页眉页脚
        recyclerView.adapter = repoAdapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = ReposLoadStateAdapter { repoAdapter.retry() }
        )
        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )
        bindList(
            header = header,
            repoAdapter = repoAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun AppActivityListBinding.bindSearch(
        uiState: StateFlow<UiState>,
        onQueryChanged: (UiAction.Search) -> Unit
    ) {
        searchRepoEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        searchRepoEdit.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            uiState
                .map {
                    it.query
                }
                .distinctUntilChanged()
                .collect(searchRepoEdit::setText)
        }
    }


    private fun AppActivityListBinding.updateRepoListFromInput(onQueryChanged: (UiAction.Search) -> Unit) {
        searchRepoEdit.text.trim().let {
            if (it.isNotEmpty()) {
                recyclerView.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = it.toString()))
            }
        }
    }

    private fun AppActivityListBinding.bindList(
        header: ReposLoadStateAdapter,
        repoAdapter: ReposAdapter,
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<UiModel>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        btnRefresh.setOnClickListener { repoAdapter.refresh() }
        retry.setOnClickListener { repoAdapter.retry() }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })
        val notLoading = repoAdapter.loadStateFlow
            .asRemotePresentationState()
            .map {
                it == RemotePresentationState.PRESENTED
            }

        val hasNotScrolledForCurrentSearch = uiState
            .map {
                it.hasNotScrolledForCurrentSearch
            }.distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()

        lifecycleScope.launch {
            // darcyRefactor: 显示分页数据
            pagingData.collectLatest(repoAdapter::submitData)
        }

        lifecycleScope.launch {
            // darcyRefactor: 滚动到顶部
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) recyclerView.scrollToPosition(0)
            }
        }

        lifecycleScope.launch {
            // darcyRefactor: 显示加载状态
            repoAdapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && repoAdapter.itemCount > 0 }
                    ?: loadState.prepend

                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && repoAdapter.itemCount == 0
                // show empty list
                emptyList.isVisible = isListEmpty
                // Only show the list if refresh succeeds, either from the the local db or the remote.
                recyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                progress.isVisible = loadState.mediator?.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                retry.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && repoAdapter.itemCount == 0
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(context, "Whoops ${it.error}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}