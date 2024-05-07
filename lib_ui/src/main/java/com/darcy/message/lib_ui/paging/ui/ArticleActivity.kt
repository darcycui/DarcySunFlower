package com.darcy.message.lib_ui.paging.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.darcy.message.lib_im.databinding.LibUiActivityArticlesBinding
import com.darcy.message.lib_ui.paging.adapter.ArticleAdapter
import com.darcy.message.lib_ui.paging.viewmodel.ArticleViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import androidx.activity.viewModels
import com.darcy.message.lib_ui.paging.Injection

class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LibUiActivityArticlesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel by viewModels<ArticleViewModel>(
            factoryProducer = { Injection.provideViewModelFactory(owner = this) }
        )

        val items = viewModel.itemsPaging
        val articleAdapter = ArticleAdapter()

        binding.bindAdapter(articleAdapter = articleAdapter)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                items.collectLatest {
                    // 通知adapter数据变化
                    articleAdapter.submitData(it)
                }
            }
        }

        binding.btnRefresh.setOnClickListener {
            articleAdapter.refresh()
        }
    }
}

private fun LibUiActivityArticlesBinding.bindAdapter(
    articleAdapter: ArticleAdapter
) {
    list.adapter = articleAdapter
    list.layoutManager = LinearLayoutManager(list.context)
    val decoration = DividerItemDecoration(list.context, DividerItemDecoration.VERTICAL)
    list.addItemDecoration(decoration)
}