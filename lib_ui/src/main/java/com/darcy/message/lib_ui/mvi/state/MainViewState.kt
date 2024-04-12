package com.darcy.message.lib_ui.mvi.state

import com.darcy.message.lib_ui.mvi.bean.NewsItem
import com.darcy.message.lib_ui.mvi.state.common.FetchStatus

data class MainViewState(
    val fetchStatus: FetchStatus = FetchStatus.NotFetched,
    val newsList: List<NewsItem> = emptyList()
) {
}

sealed class MainViewEvent {
    data class ShowSnackbarEvent(val message: String) : MainViewEvent()
    data class ShowToastEvent(val message: String) : MainViewEvent()
}

sealed class MainViewIntent {
    data class NewsItemClickedIntent(val newsItem: NewsItem) : MainViewIntent()
    data object FabClickedIntent : MainViewIntent()
    data object OnSwipeRefreshIntent : MainViewIntent()
    data object FetchNewsIntent : MainViewIntent()
}