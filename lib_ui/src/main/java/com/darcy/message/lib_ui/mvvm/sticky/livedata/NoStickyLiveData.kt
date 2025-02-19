package com.darcy.message.lib_ui.mvvm.sticky.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * 通过反射在observer中找到mObservers对象和当前mVersion，
 * 然后便可以在这里将mVersion赋值给mLastVersion。
 */
class NoStickyLiveData<T>(value1: T) : MutableLiveData<T>(value1) {
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        // 注意先调用super再hook 否则无法获取到observerWrapper
        hook(observer)
//        solveStick(observer)
    }

    /**
     * 使用反射将mLastVersion的值改为和mVersion相同，就可以解决粘性数据问题
     */
    @Throws(java.lang.Exception::class)
    private fun <T> hook(observer: Observer<T>) {
        try {//get wrapper's version
            val classLiveData = LiveData::class.java
            val fieldObservers = classLiveData.getDeclaredField("mObservers")
            fieldObservers.isAccessible = true
            val objectObservers = fieldObservers[this]
            val classObservers: Class<*> = objectObservers.javaClass
            val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
            methodGet.isAccessible = true
            val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
            var objectWrapper: Any? = null
            if (objectWrapperEntry is Map.Entry<*, *>) {
                objectWrapper = objectWrapperEntry.value
            }
            if (objectWrapper == null) {
                throw NullPointerException("Wrapper can not be bull!")
            }
            val classObserverWrapper: Class<*> = objectWrapper.javaClass.superclass
            val fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion")
            fieldLastVersion.isAccessible = true
            //get livedata's version
            val fieldVersion = classLiveData.getDeclaredField("mVersion")
            fieldVersion.isAccessible = true
            val objectVersion = fieldVersion[this]
            //set wrapper's version
            fieldLastVersion[objectWrapper] = objectVersion
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    /**
     * 使用反射将mLastVersion的值改为和mVersion相同，就可以解决粘性数据问题
     */
    private fun solveStick(observer: Observer<in T>) {
        try {
            val liveDataClass = LiveData::class.java

            //先拿到observer.mLastVersion，mLastVersion在ObserverWrapper中，ObserverWrapper又存放在mObservers中

            //获取mObserversFiled
            val mObserversFiled = liveDataClass.getDeclaredField("mObservers")
            mObserversFiled.isAccessible = true

            //获取map值,this就是LiveData
            val mObserversMap: Any = mObserversFiled.get(this)

            //拿到mObserversMap的class
            val mObserversMapClass = mObserversMap.javaClass

            //获取map的get方法
            val mObserversMapGet = mObserversMapClass.getDeclaredMethod("get", Any::class.java)
            mObserversMapGet.isAccessible = true

            //执行map的get方法,获取ObserverWrapper
            var observerWrapper: Any? = null
            val invokeEntry = mObserversMapGet.invoke(mObserversMap, observer)
            if (invokeEntry != null && invokeEntry is Map.Entry<*, *>) {
                observerWrapper = invokeEntry.value
            } else {
                throw Exception("observerWrapper error")
            }

            //获取ObserverWrapper中的mLastVersion
            //由于这里获取到的的是ObserverWrapper的子类LifecycleBoundObserver或AlwaysActiveObserver
            //所以需要先用superclass获取超类
            val observerWrapperSuperClass = observerWrapper?.javaClass?.superclass
            val mLastVersionFiled = observerWrapperSuperClass?.getDeclaredField("mLastVersion")
            mLastVersionFiled?.isAccessible = true

            //获取mVersion
            val mVersionFiled = liveDataClass.getDeclaredField("mVersion")
            mVersionFiled.isAccessible = true
            //拿到mVersion的值
            val mVersion = mVersionFiled.get(this)

            //将mLastVersion的值设置成跟mVersion相同
            mLastVersionFiled?.set(observerWrapper, mVersion)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}