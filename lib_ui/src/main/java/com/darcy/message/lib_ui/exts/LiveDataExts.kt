package com.darcy.message.lib_ui.exts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> {
    return this
}