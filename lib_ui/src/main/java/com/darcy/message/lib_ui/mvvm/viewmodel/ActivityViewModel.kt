package com.darcy.message.lib_ui.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darcy.message.lib_ui.mvvm.sticky.livedata.UnPeekLiveDataNew
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {

    companion object {
        // darcyRefactor: 带粘性的LiveData
//        private val _countInner: MutableLiveData<Int> = MutableLiveData(0)
        // darcyRefactor: 去除粘性的LiveData
//        private val _countInner: SingleEventLiveData<Int> = SingleEventLiveData(0)
//        private val _countInner: NoStickyLiveData<Int> = NoStickyLiveData(0)
//        private val _countInner: UnPeekLiveData<Int> = UnPeekLiveData(0)
        private val _countInner: UnPeekLiveDataNew<Int> = UnPeekLiveDataNew(0)
        private val countLiveData = _countInner
        fun getGlobalLiveDataForStickyTest(): LiveData<Int> {
            return countLiveData
        }
    }

    val countLiveData = _countInner
    fun addCount() {
        viewModelScope.launch(Dispatchers.IO) {
            _countInner.postValue(_countInner.value?.plus(1) ?: -1)
        }
    }

}