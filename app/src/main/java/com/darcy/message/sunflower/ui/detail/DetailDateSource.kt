package com.darcy.message.sunflower.ui.detail

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.sunflower.ui.detail.bean.DetailBean
import com.darcy.message.sunflower.ui.detail.repository.DetailRepository
import java.lang.RuntimeException
import kotlin.math.max

private const val STARTING_KEY: Int = 1

class DetailDateSource(private val itemDao: ItemDao) : PagingSource<Int, DetailBean>() {
    override fun getRefreshKey(state: PagingState<Int, DetailBean>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val article = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = article.id - (state.config.pageSize / 2))
    }

    /**
     * Makes sure the paging key is never less than [STARTING_KEY]
     */
    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DetailBean> {
        return try {
            //页码未定义置为1
            val currentPage = params.key ?: STARTING_KEY
            //仓库层请求数据
            val itemList = DetailRepository(itemDao).loadData(currentPage)
            //当前页码 小于 总页码 页面加1
            val nextPage = if (currentPage < 3) {
                currentPage + 1
            } else {
                //没有更多数据
                null
            }
            if (itemList != null) {
                LoadResult.Page(
                    data = createDetailBeanList(itemList),
                    prevKey = null,
                    nextKey = nextPage
                )
            } else {
                LoadResult.Error(throwable = RuntimeException("Paging End"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }

    private fun createDetailBeanList(itemList: List<Item>): List<DetailBean> {
        return itemList.map { DetailBean().generate(it) }
    }
}