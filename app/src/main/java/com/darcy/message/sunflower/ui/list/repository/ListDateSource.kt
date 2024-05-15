package com.darcy.message.sunflower.ui.list.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.sunflower.ui.list.bean.ListBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.max

private const val START_KEY: Int = 8

const val ITEMS_PER_PAGE = 10

class ListDateSource(private val itemDao: ItemDao) : PagingSource<Int, ListBean>() {
    override fun getRefreshKey(state: PagingState<Int, ListBean>): Int? {
//        val anchorPosition = state.anchorPosition ?: return null
//        val article = state.closestItemToPosition(anchorPosition) ?: return null
//        val refreshKey = article.id - (state.config.pageSize / 2)
//        logI(message = "getRefreshKey=$refreshKey")
//        return ensureValidKey(key = refreshKey)
        return null
    }

    /**
     * Makes sure the paging key is never less than [START_KEY]
     */
    private fun ensureValidKey(key: Int) = max(START_KEY, key)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListBean> {
        try {
            // get page number
            val page = when (params) {
                is LoadParams.Refresh -> {
                    logV(message = "Refresh")
                    START_KEY
                }

                is LoadParams.Prepend -> {
                    logV(message = "Prepend")
                    params.key
                }

                is LoadParams.Append -> {
                    logV(message = "Append")
                    params.key
                }

                else -> {
                    logV(message = "Unknown")
                    START_KEY
                }
            }

            // repository layer request data
            delay(2_000)
            val itemList = itemDao.getItemsByPage(page, ITEMS_PER_PAGE) ?: listOf()
            // null means no more data.
            val nextPage = if (itemList.isNotEmpty()) {
                page + 1
            } else {
                null
            }
            // null means no more data.
            val prevPage = if (page > 1) page - 1 else null
            logD(message = "page=$page nextPage=$nextPage prevPage=$prevPage itemList=${itemList.size}")
            // the simulation failed load data.
            if ((nextPage == START_KEY || prevPage == START_KEY) and (showError)) {
                showError = false
                return LoadResult.Error(throwable = Exception("Failed load data."))
            }
            // the simulation failed load more data
            if ((nextPage != START_KEY && prevPage != START_KEY) and (showErrorLoadMore)) {
                showErrorLoadMore = false
                return LoadResult.Error(throwable = Exception("Failed load more data."))
            }
            return LoadResult.Page(
                data = createDetailBeanList(itemList),
                prevKey = prevPage,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return LoadResult.Error(throwable = e)
        }
    }

    private fun createDetailBeanList(itemList: List<Item>): List<ListBean> {
        return itemList.map { ListBean().generate(it) }
    }

    companion object {
        var showErrorLoadMore = true
        var showError = true
    }
}