package com.darcy.message.lib_ui.mvi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darcy.message.lib_ui.mvi.bean.NewsItem
import com.darcy.message.lib_ui.mvi.event.LiveEvents
import com.darcy.message.lib_ui.mvi.state.MainViewIntent
import com.darcy.message.lib_ui.mvi.state.MainViewEvent
import com.darcy.message.lib_ui.mvi.state.MainViewState
import com.darcy.message.lib_ui.mvi.utils.FetchStatus
import com.darcy.message.lib_ui.mvi.utils.PageState
import com.darcy.message.lib_ui.mvi.utils.asLiveData
import com.darcy.message.lib_ui.mvi.utils.setEvent
import com.darcy.message.lib_ui.mvi.utils.setState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var count: Int = 0
    private val _viewStates: MutableLiveData<MainViewState> = MutableLiveData(MainViewState())
    val viewStates = _viewStates.asLiveData()
    private val _viewEvents: LiveEvents<MainViewEvent> = LiveEvents()
    val viewEvents = _viewEvents.asLiveData()

    fun dispatch(intent: MainViewIntent) {
        when (intent) {
            is MainViewIntent.NewsItemClickedIntent -> newsItemClicked(intent.newsItem)
            MainViewIntent.FabClickedIntent -> fabClicked()
            MainViewIntent.OnSwipeRefreshIntent -> fetchNews()
            MainViewIntent.FetchNewsIntent -> fetchNews()
        }
    }

    private fun newsItemClicked(newsItem: NewsItem) {
        _viewEvents.setEvent (MainViewEvent.ShowSnackbarEvent(newsItem.title))
    }

    private fun fabClicked() {
        count++
        _viewEvents.setEvent (MainViewEvent.ShowToastEvent(message = "Fab clicked count $count"))
    }

    private fun fetchNews() {
        _viewStates.setState {
            copy(fetchStatus = FetchStatus.Fetching)
        }
        viewModelScope.launch {
            when (val result = getMockApiResponse()) {
                is PageState.Error -> {
                    _viewStates.setState {
                        copy(fetchStatus = FetchStatus.Fetched)
                    }
                    _viewEvents.setEvent (MainViewEvent.ShowToastEvent(message = result.message))
                }

                is PageState.Success -> {
                    _viewStates.setState {
                        copy(fetchStatus = FetchStatus.Fetched, newsList = result.data)
                    }
                }
            }
        }
    }

    private suspend fun getMockApiResponse(): PageState<List<NewsItem>> {
        delay(2_000)
        return if (count % 2 == 0) {
            PageState.Success(listOf(NewsItem("title", "description", "imageUrl")))
        } else {
            PageState.Error("Failed to fetch news")
        }
    }

}