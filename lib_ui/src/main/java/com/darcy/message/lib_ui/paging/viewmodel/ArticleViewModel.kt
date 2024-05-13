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
import androidx.paging.insertFooterItem
import androidx.paging.insertHeaderItem
import androidx.paging.insertSeparators
import com.darcy.message.lib_ui.paging.entity.IEntity
import com.darcy.message.lib_ui.paging.repoisitory.ArticlePagingSource
import com.darcy.message.lib_ui.paging.entity.IEntity.Article
import com.darcy.message.lib_ui.paging.repoisitory.firstArticleCreatedTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

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
    @RequiresApi(Build.VERSION_CODES.O)
    val itemsPaging = Pager(
        config = PagingConfig(
            // pageSize
            pageSize = ITEMS_PER_PAGE,
            // initSize by default: 3 times the pageSize
            initialLoadSize = 3 * ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        // Create a PagingSource object and create a new one each time
        pagingSourceFactory = { ArticlePagingSource(repository) }
    ).flow
        .map { pagingData ->
            pagingData.insertHeaderItem(
                item = Article(
                    id = -1,
                    title = "###Header Article###",
                    description = "xxxxxx",
                    created = LocalDateTime.now()
                )
            )
                .insertFooterItem(
                    item = Article(
                        id = -1,
                        title = "###Footer Article###",
                        description = "xxxxxx",
                        created = LocalDateTime.now()
                    )
                )
                .insertSeparators<Article, IEntity> { before: Article?, after: Article? ->
                    if (before == null || after == null) {
                        return@insertSeparators null
                    }
                    if (before.id != 0 && before.id % 10 == 0) {
                        return@insertSeparators IEntity.SeparatorEntity(
                            id = before.id,
                            title = "Separator"
                        )
                    }
                    return@insertSeparators null
                }
        }
        .cachedIn(viewModelScope)
}