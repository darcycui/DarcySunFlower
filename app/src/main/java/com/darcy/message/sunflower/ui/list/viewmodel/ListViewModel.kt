package com.darcy.message.sunflower.ui.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.tables.Item
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
) : ViewModel() {
    /**
     * Paginated data from ListDateSource
     * Cached the flow to viewModelScope
     */
    val itemsPaging: Flow<PagingData<ListBean>> =
        listRepository.getItemsPaging().cachedIn(viewModelScope)


    /**
     * Paginated data from ItemDao
     * Cached the flow to viewModelScope
     */
    val itemsPagingFromRoom: Flow<PagingData<Item>> =
        listRepository.getItemsPagingFromRoom().cachedIn(viewModelScope)


    /**
     * Paginated data from ItemDao
     * Cached the flow to viewModelScope
     */
    val itemsPagingFromRoomAndHttp: Flow<PagingData<Item>> =
        listRepository.getPagingFromRoomAndHttp().cachedIn(viewModelScope)

}