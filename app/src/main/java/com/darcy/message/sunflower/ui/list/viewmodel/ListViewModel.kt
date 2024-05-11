package com.darcy.message.sunflower.ui.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.sunflower.ui.list.repository.ListDateSource
import com.darcy.message.sunflower.ui.list.repository.ITEMS_PER_PAGE
import com.darcy.message.sunflower.ui.list.bean.ListBean
import com.darcy.message.sunflower.ui.list.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * use hilt inject ViewModel
 */
@HiltViewModel
class ListViewModel @Inject constructor(
    private val listRepository: ListRepository,
    private val itemDao: ItemDao
) : ViewModel() {
    /**
     * Paginated data flow
     * Cached the flow to viewModelScope
     */
    val itemsPaging: Flow<PagingData<ListBean>> = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            initialLoadSize = 3 * ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        // create pagingSource
        pagingSourceFactory = { ListDateSource(listRepository) }
    ).flow.cachedIn(viewModelScope)

    /**
     * Paginated data flow
     * Cached the flow to viewModelScope
     */
    val itemsPagingNew: Flow<PagingData<Item>> = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            initialLoadSize = 3 * ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        // create pagingSource by itemDao
        pagingSourceFactory = { itemDao.getItemsPagingSource()}
    ).flow.cachedIn(viewModelScope)
}