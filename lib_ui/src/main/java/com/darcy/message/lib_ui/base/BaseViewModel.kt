package com.darcy.message.lib_ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darcy.message.lib_ui.mvi.intent.IUiIntent
import com.darcy.message.lib_ui.mvi.state.IUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : IUiState, I : IUiIntent> : ViewModel() {
    private val _uiStateFLow = MutableStateFlow<S>(initUiState())
    val uiStateFlow: StateFlow<S>
        get() = _uiStateFLow

    // 这里使用Channel接收uiIntent
    private val _uiIntentFlow: Channel<I> = Channel()
    private val uiIntentFlow: Flow<I>
        get() = _uiIntentFlow.receiveAsFlow() // Channel转为Flow


    init {
        // 收集uiIntentFlow
        viewModelScope.launch {
            uiIntentFlow.collect {
                handleUiIntent(it)
            }
        }
    }
    // 发送Intent给ViewModel
    fun sendUiIntent(intent: I) {
        viewModelScope.launch {
            _uiIntentFlow.send(intent)
        }
    }

    // 发送State给Activity
    fun sendUiState(copy2: S.() -> S) {
        _uiStateFLow.update { _uiStateFLow.value.copy2() }
    }

    abstract fun initUiState(): S
    abstract fun handleUiIntent(intent: I)
}