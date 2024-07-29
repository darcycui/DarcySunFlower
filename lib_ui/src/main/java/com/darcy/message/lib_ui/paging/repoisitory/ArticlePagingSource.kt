package com.darcy.message.lib_ui.paging.repoisitory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_ui.paging.entity.IEntity.Article
import kotlinx.coroutines.delay
import kotlin.math.max

/**
 * PagingSource deal with page logic
 * The data is fetched from Database only.(No internet data here)
 */
class ArticlePagingSource(val repository: ArticleRepository) : PagingSource<Int, Article>() {
    private val START_KEY: Int = 50
    companion object {
        private var emitError = true
        private var emitLoadMoreError = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        // Start paging with the STARTING_KEY if this is the first load
        val start = params.key ?: START_KEY
        val range = when (params) {
            is LoadParams.Refresh -> {
                logD(message = "Refresh")
                START_KEY.until(START_KEY + params.loadSize)
            }

            is LoadParams.Prepend -> {
                logD(message = "Prepend")
                val prependEnd = start - params.loadSize
                prependEnd.until(start).also {
                    logD(message = "page=$start nextPage=${it.first}")
                }
            }

            is LoadParams.Append -> {
                logD(message = "Append")
                // Load as many items as hinted by params.loadSize
                val appendEnd = start + params.loadSize
                // darcyRefactor 注意这里是从start + 1开始
                (start + 1).until(appendEnd).also {
                    logD(message = "page=$start nextPage=${it.last}")
                }
            }

            else -> {
                logD(message = "Unknown")
                START_KEY.until(START_KEY + params.loadSize)
            }
        }
        // 第一次请求返回异常
        if (start == START_KEY && emitError){
            delay(1000)
            emitError = false
            return LoadResult.Error(IllegalStateException("Failed to load data."))
        }

        // 第一次 loadMore 返回异常
        if (start != START_KEY && emitLoadMoreError){
            delay(1000)
            emitLoadMoreError = false
            return LoadResult.Error(IllegalStateException("Failed to load more data."))
        }
        // darcyRefactor 根据range从数据库获取数据
        return LoadResult.Page(
            data = repository.getRemoteArticles(range),
            // Make sure we don't try to load items behind the STARTING_KEY
            prevKey = if (range.first <= 0) {
                null
            } else {
                range.first
            },
            nextKey = if (range.last >= 100) {
                null
            } else {
                range.last
            }
        )
    }

    /**
     * Makes sure the paging key is never less than [START_KEY]
     */
    private fun ensureValidKey(key: Int) = max(START_KEY, key)

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // In our case we grab the item closest to the anchor position
        // then return its id - (state.config.pageSize / 2) as a buffer
//        val anchorPosition = state.anchorPosition ?: return null
//        val article = state.closestItemToPosition(anchorPosition) ?: return null
//        return ensureValidKey(key = article.id - (state.config.pageSize / 2))
        return null
    }
}