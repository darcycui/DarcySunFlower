package com.darcy.message.lib_ui.mvvm.sticky.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * 为每个传入的observer对象携带一个布尔类型的值，作为其是否能进入observe方法的开关。
 * 每当有一个新的observer存进来的时候，开关默认关闭。
 * 每次setValue后，打开所有Observer的开关，允许所有observe执行。
 * 同时方法进去后，关闭当前执行的observer开关，即不能对其第二次执行了，除非你重新setValue。
 * 通过这种机制，使得 不用反射技术实现LiveData的非粘性态 成为了可能。
 *
 * 链接：https://www.jianshu.com/p/d0244c4c7cc9
 */
class UnPeekLiveData<T>(value1: T) : MutableLiveData<T>(value1) {
    protected var isAllowNullValue: Boolean = false

    private val observers = HashMap<Int, Boolean?>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val storeId =
            System.identityHashCode(observer) //源码这里是activity.getViewModelStore()，是为了保证同一个ViewModel环境下"唯一可信源"
        observe(storeId, owner, observer)
    }

    private fun observe(
        storeId: Int,
        owner: LifecycleOwner,
        observer: Observer<in T>
    ) {
        if (observers[storeId] == null) {
            observers[storeId] = true
        }

        super.observe(owner) { t: T ->
            if (!observers[storeId]!!) {
                observers[storeId] = true
                if (t != null || isAllowNullValue) {
                    observer.onChanged(t)
                }
            }
        }
    }

    override fun setValue(value: T) {
        if (value != null || isAllowNullValue) {
            for (entry in observers.entries) {
                entry.setValue(false)
            }
            super.setValue(value)
        }
    }

    fun clear() {
        super.setValue(null)
    }
}