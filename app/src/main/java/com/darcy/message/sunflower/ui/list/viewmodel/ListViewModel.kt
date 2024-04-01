package com.darcy.message.sunflower.ui.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.sunflower.ui.list.ListDateSource
import com.darcy.message.sunflower.ui.list.ITEMS_PER_PAGE
import com.darcy.message.sunflower.ui.list.bean.ListBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * use hilt inject ViewModel
 */
@HiltViewModel
class ListViewModel @Inject constructor (private val itemDao: ItemDao) : ViewModel() {
    /**
     * 分页数据流
     * 注意这里 缓存到viewModelScope
     */
    val itemsPaging: Flow<PagingData<ListBean>> = Pager(
        config = PagingConfig(ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = { ListDateSource(itemDao) }
    ).flow.cachedIn(viewModelScope)
}