package com.darcy.message.lib_ui.paging.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import com.darcy.message.lib_ui.paging.repoisitory.ArticleRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.darcy.message.lib_ui.paging.repoisitory.ArticlePagingSource
import com.darcy.message.lib_ui.paging.entity.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val ITEMS_PER_PAGE = 10

/**
 * ViewModel for the [ArticleActivity] screen.
 * The ViewModel works with the [ArticleRepository] to get the data.
 */
class ArticleViewModel(
    repository: ArticleRepository,
) : ViewModel() {

    /**
     * Stream of [Article]s for the UI.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    val items: StateFlow<List<Article>> = repository.getArticleStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )

    /**
     * paging data flow
     * use [cachedIn] cached to viewModelScope
     */
    val itemsPaging: Flow<PagingData<Article>> = Pager(
        config = PagingConfig(
            // pageSize
            pageSize = ITEMS_PER_PAGE,
            // initSize by default: 3 times the pageSize
            initialLoadSize = 3 * ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        // Create a PagingSource object and create a new one each time
        pagingSourceFactory = { ArticlePagingSource(repository) }
    ).flow.cachedIn(viewModelScope)
}