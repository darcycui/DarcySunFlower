package com.darcy.message.sunflower.ui.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.sunflower.ui.list.bean.ListBean
import com.darcy.message.sunflower.ui.list.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    val itemsPagingFromRoom: Flow<PagingData<ListBean>> =
        listRepository.getItemsPagingFromRoom().cachedIn(viewModelScope)

    /**
     * Paginated data from ItemDao
     * Cached the flow to viewModelScope
     */
    val itemsPagingFromRoomDao: Flow<PagingData<ListBean>> =
        listRepository.getItemsPagingFromRoomDao().map { pagingData->
            pagingData.map { item ->
                logD(message = "item-->$item")
                ListBean().generate(item)
            }
        }.cachedIn(viewModelScope)

    /**
     * Paginated data from RepoPagingSource
     */
    val itemsPagingFromHttp: Flow<PagingData<ListBean>> =
        listRepository.getItemsPagingFromHttp("Android").map { pagingData ->
            pagingData.map { item ->
                ListBean().generate(item)
            }
        }.cachedIn(viewModelScope)


    /**
     * Paginated data from RepoDao and Http
     * Cached the flow to viewModelScope
     */
    val itemsPagingFromRoomAndHttp: Flow<PagingData<ListBean>> =
        listRepository.getPagingFromRoomAndHttp().map { pagingData ->
            pagingData.map {item->
                logD(message = "repo-->$item")
                ListBean().generate(item)
            }
        }.cachedIn(viewModelScope)

}