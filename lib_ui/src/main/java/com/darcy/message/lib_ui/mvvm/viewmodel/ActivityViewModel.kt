package com.darcy.message.lib_ui.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darcy.message.lib_ui.exts.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {
    private val _countInner: MutableLiveData<Int> = MutableLiveData(0)
    val countLiveData = _countInner.asLiveData()
    fun addCount() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1)
            _countInner.postValue(_countInner.value?.plus(1) ?: -1)
        }
    }

}