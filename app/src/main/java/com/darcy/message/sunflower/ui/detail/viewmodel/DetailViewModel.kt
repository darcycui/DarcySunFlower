package com.darcy.message.sunflower.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.sunflower.ui.detail.DetailDateSource
import com.darcy.message.sunflower.ui.detail.bean.DetailBean
import kotlinx.coroutines.flow.Flow

private const val ITEMS_PER_PAGE = 50

class DetailViewModel(private val itemDao: ItemDao) : ViewModel() {
    /**
     * 分页数据流
     * 注意这里 缓存到viewModelScope
     */
    val itemsPaging: Flow<PagingData<DetailBean>> = Pager(
        config = PagingConfig(ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = { DetailDateSource(itemDao) }
    ).flow.cachedIn(viewModelScope)
}