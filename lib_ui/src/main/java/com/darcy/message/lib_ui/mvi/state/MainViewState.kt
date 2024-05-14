package com.darcy.message.lib_ui.mvi.state

import com.darcy.message.lib_ui.mvi.bean.NewsItem
import com.darcy.message.lib_ui.mvi.state.common.FetchStatus

/**
 * State: long lifecycle, consumed multiple times in the view
 * need sticky
 */
data class MainViewState(
    val fetchStatus: FetchStatus = FetchStatus.NotFetched,
    val newsList: List<NewsItem> = emptyList()
) {
}

/**
 * Event:short lifecycle, consumed only once in the view
 * do not need sticky
 */
sealed class MainViewEvent {
    data class ShowSnackbarEvent(val message: String) : MainViewEvent()
    data class ShowToastEvent(val message: String) : MainViewEvent()
}

/**
 * Intent: encapsulation of user action that triggers state/event changes
 */
sealed class MainViewIntent {
    data class NewsItemClickedIntent(val newsItem: NewsItem) : MainViewIntent()
    data object FabClickedIntent : MainViewIntent()
    data object OnSwipeRefreshIntent : MainViewIntent()
    data object FetchNewsIntent : MainViewIntent()
}