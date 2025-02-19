package com.darcy.message.lib_ui.mvvm.sticky.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 解决LiveData粘性的方案
 * 1.在observe/observeForever时创建新的LiveData，并且根据observer保存该LiveData到mObserverMap中，而且该LiveData订阅相关的observer；
 * 2.当postValue/setValue时，遍历mObserverMap的所有LiveData，并把值设置给LiveData;
 *
 * 原文链接：<a href="https://blog.csdn.net/hewuzhao/article/details/117165379">...</a>
 * @param <T>
 */
public class UnFlowLiveDataHelper<T> {
    private final Handler mMainHandler;
    private T mValue;
    private final ConcurrentHashMap<Observer<? super T>, MutableLiveData<T>> mObserverMap;

    public UnFlowLiveDataHelper() {
        mMainHandler = new Handler(Looper.getMainLooper());
        mObserverMap = new ConcurrentHashMap<>();
    }

    @MainThread
    public void observeForever(@NonNull Observer<? super T> observer) {
        checkMainThread("observeForever");
        MutableLiveData<T> liveData = new MutableLiveData<>();
        // 该LiveData也observeForever该observer，这样setValue时，能把value回调到onChanged中
        liveData.observeForever(observer);
        mObserverMap.put(observer, liveData);
    }

    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        checkMainThread("observe");
        Lifecycle lifecycle = owner.getLifecycle();
        if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
            // ignore
            return;
        }
        lifecycle.addObserver(new LifecycleObserver() {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            public void onDestroy() {
                mObserverMap.remove(observer);
                lifecycle.removeObserver(this);
            }
        });
        // 创建新的LiveData，而且该LiveData订阅相关的observer
        MutableLiveData<T> liveData = new MutableLiveData<>();
        liveData.observe(owner, observer);

        // 保存该LiveData到mObserverMap中
        mObserverMap.put(observer, liveData);
    }

    @MainThread
    public void removeObserver(@NonNull final Observer<? super T> observer) {
        checkMainThread("removeObserver");
        mObserverMap.remove(observer);
    }

    public T getValue() {
        return mValue;
    }

    public void clearValue() {
        mValue = null;
    }

    @MainThread
    public void setValue(T value) {
        checkMainThread("setValue");
        mValue = value;
        // 遍历所有LiveData，并把value设置给LiveData
        for (MutableLiveData<T> liveData : mObserverMap.values()) {
            liveData.setValue(value);
        }
    }

    public void postValue(T value) {
        mMainHandler.post(() -> setValue(value));
    }

    private void checkMainThread(String methodName) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("UnFlowLiveData, Cannot invoke " + methodName
                    + " on a background thread");
        }
    }
}

