package com.darcy.message.lib_ui.mvi.state.common


//LCE -> Loading/Content/Error
sealed class PageState<out T> {
    data class Success<T>(val data: T) : PageState<T>()
    data class Error<T>(val message: String) : PageState<T>() {
        constructor(t: Throwable) : this(t.message ?: "")
    }
}

sealed class FetchStatus {
    data object Fetching : FetchStatus()
    data object Fetched : FetchStatus()
    data object NotFetched : FetchStatus()
}

