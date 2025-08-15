package com.darcy.message.sunflower.ui.list.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.sunflower.ui.list.bean.UiModel
import com.darcy.message.sunflower.ui.list.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * use hilt inject ViewModel
 */
@HiltViewModel
class ListViewModel @Inject constructor(
    private val listRepository: ListRepository,
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : ViewModel() {
    // 用户意图
    val uiAction: (UiAction) -> Unit

    // UI状态
    val uiState: StateFlow<UiState>

    // 分页数据
    val pagingDataFlow: Flow<PagingData<UiModel>>

    init {
        // 从 savedStateHandle 中获取状态
        val initialQuery: String = savedStateHandle[LAST_SEARCH_QUERY] ?: DEFAULT_QUERY
        val lastQueryScrolled: String = savedStateHandle[LAST_QUERY_SCROLLED] ?: DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<UiAction>()
        // 搜索
        val searches = actionStateFlow.filterIsInstance<UiAction.Search>().distinctUntilChanged()
            .onStart {
                emit(UiAction.Search(initialQuery))
            }
        // 滚动
        val queriesScrolled =
            actionStateFlow.filterIsInstance<UiAction.Scroll>()
                .distinctUntilChanged()
                .shareIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                    replay = 1
                ).onStart {
                    emit(UiAction.Scroll(lastQueryScrolled))
                }
        // 分页数据
        pagingDataFlow = searches.flatMapLatest {
            searchRepo(it.query)
        }.cachedIn(viewModelScope)

        // UI状态
        uiState = combine(searches, queriesScrolled, ::Pair)
            .map { (search, scroll) ->
                UiState(
                    query = search.query, lastQueryScrolled = scroll.currentQuery,
                    hasNotScrolledForCurrentSearch = scroll.currentQuery != search.query
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = UiState()
            )
        // 用户意图
        uiAction = { action ->
            viewModelScope.launch {
                actionStateFlow.emit(action)
            }
        }
    }

    override fun onCleared() {
        // 保存状态到 savedStateHandle
        savedStateHandle[LAST_SEARCH_QUERY] = uiState.value.query
        savedStateHandle[LAST_QUERY_SCROLLED] = uiState.value.lastQueryScrolled
        super.onCleared()
    }

    private fun searchRepo(queryString: String): Flow<PagingData<UiModel>> =
        listRepository.getPagingFromRoomAndHttp(queryString).map { pagingData ->
            pagingData.map { item ->
                logD(message = "repo-->$item")
                UiModel().generate(item)
            }
        }

    /**
     * Paginated data from ListDateSource
     * Cached the flow to viewModelScope
     */
    val itemsPagingFromRoom: Flow<PagingData<UiModel>> =
        listRepository.getItemsPagingFromRoom().cachedIn(viewModelScope)

    /**
     * Paginated data from ItemDao
     * Cached the flow to viewModelScope
     */
    val itemsPagingFromRoomDao: Flow<PagingData<UiModel>> =
        listRepository.getItemsPagingFromRoomDao(DEFAULT_QUERY).map { pagingData ->
            pagingData.map { item ->
                logD(message = "item-->$item")
                UiModel().generate(item)
            }
        }.cachedIn(viewModelScope)

    /**
     * Paginated data from RepoPagingSource
     */
    val itemsPagingFromHttp: Flow<PagingData<UiModel>> =
        listRepository.getItemsPagingFromHttp("Android").map { pagingData ->
            pagingData.map { item ->
                UiModel().generate(item)
            }
        }.cachedIn(viewModelScope)


    /**
     * Paginated data from RepoDao and Http
     * Cached the flow to viewModelScope
     */
    val itemsPagingFromRoomAndHttp: Flow<PagingData<UiModel>> =
        listRepository.getPagingFromRoomAndHttp(DEFAULT_QUERY).map { pagingData ->
            pagingData.map { item ->
                logD(message = "repo-->$item")
                UiModel().generate(item)
            }
        }.cachedIn(viewModelScope)

}

sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()
}

data class UiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)

private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = "Android"