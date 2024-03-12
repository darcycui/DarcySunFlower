package com.darcy.message.lib_ui.paging.repoisitory

import android.os.Build
import androidx.annotation.RequiresApi
import com.darcy.message.lib_ui.paging.ArticlePagingSource
import com.darcy.message.lib_ui.paging.entity.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
private val firstArticleCreatedTime = LocalDateTime.now()

/**
 * Repository class that mimics fetching [Article] instances from an asynchronous source.
 */
class ArticleRepository {
    /**
     * Exposed singular stream of [Article] instances
     */
    @RequiresApi(Build.VERSION_CODES.O)
    val articleStream: Flow<List<Article>> = flowOf(
        (0..500).map { number ->
            Article(
                id = number,
                title = "Article $number",
                description = "This describes article $number",
                created = firstArticleCreatedTime.minusDays(number.toLong())
            )
        }
    )

    /**
     * 创建PagingSource对象 每次都新建
     */
    fun articlePagingSource() = ArticlePagingSource()
}