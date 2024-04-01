package com.darcy.message.sunflower.ui.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.sunflower.ui.list.bean.DetailBean
import com.darcy.message.sunflower.ui.list.repository.ListRepository
import kotlin.math.max

private const val STARTING_KEY: Int = 1

const val ITEMS_PER_PAGE = 20

class DetailDateSource(private val itemDao: ItemDao) : PagingSource<Int, DetailBean>() {
    override fun getRefreshKey(state: PagingState<Int, DetailBean>): Int? {
//        val anchorPosition = state.anchorPosition ?: return null
//        val article = state.closestItemToPosition(anchorPosition) ?: return null
//        val refreshKey = article.id - (state.config.pageSize / 2)
//        logI(message = "getRefreshKey=$refreshKey")
//        return ensureValidKey(key = refreshKey)
        return null
    }

    /**
     * Makes sure the paging key is never less than [STARTING_KEY]
     */
    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DetailBean> {
        try {
            when (params) {
                is LoadParams.Refresh -> {
                    logV(message = "Refresh")
                }

                is LoadParams.Prepend -> {
                    logV(message = "Prepend")
                }

                is LoadParams.Append -> {
                    logV(message = "Append")
                }

                else -> {
                    logV(message = "Unknown")
                }
            }
            //页码未定义置为1
            val page = params.key ?: STARTING_KEY
            //仓库层请求数据
//            delay(1_000)
            val itemList = ListRepository(itemDao).loadData(page, ITEMS_PER_PAGE)
            //下一页 null 没有更多数据
            val nextPage = if (itemList.isNotEmpty()) {
                page + 1
            } else {
                null
            }
            // 上一页 null 没有更多数据
            val prevPage = if (page > 1) page - 1 else null
            logD(message = "page=$page nextPage=$nextPage prevPage=$prevPage itemList=${itemList.size}")
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

    private fun createDetailBeanList(itemList: List<Item>): List<DetailBean> {
        return itemList.map { DetailBean().generate(it) }
    }
}