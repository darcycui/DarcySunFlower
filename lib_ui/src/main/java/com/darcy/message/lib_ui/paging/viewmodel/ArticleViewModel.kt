package com.darcy.message.lib_ui.paging.viewmodel

import com.darcy.message.lib_ui.paging.repoisitory.ArticleRepository
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.darcy.message.lib_ui.paging.entity.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val ITEMS_PER_PAGE = 50

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
    val items: StateFlow<List<Article>> = repository.articleStream
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
        config = PagingConfig(ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = { repository.articlePagingSource() }
    ).flow.cachedIn(viewModelScope)
}