package com.darcy.message.lib_ui.mvvm.sticky.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 只会消费一次的 LiveData
 */
class SingleEventLiveData<T>(value1: T) : MutableLiveData<T>(value1) {
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    /**
     * 只重写 setValue 方法
     * postValue 最终还会调用 setValue 方法
     */
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }


}