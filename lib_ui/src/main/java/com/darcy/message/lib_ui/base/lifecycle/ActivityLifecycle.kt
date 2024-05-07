package com.darcy.message.lib_ui.base.lifecycle

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI

open class ActivityLifecycle : AppCompatActivity() {
    protected val TAG: String by lazy {
        this::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                logI(TAG, "$TAG onCreate")
            }

            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                logD(TAG, "$TAG onStart")
            }

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                logD(TAG, "$TAG onResume")
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                logD(TAG, "$TAG onPause")
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                logD(TAG, "$TAG onStop")
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                logE(TAG, "$TAG onDestroy")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        logD(TAG, "$TAG onCreate2")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logD(TAG, "$TAG onSaveInstanceState")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        logD(TAG, "$TAG onSaveInstanceState2")
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        logD(TAG, "$TAG onRestoreInstanceState")
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        logD(TAG, "$TAG onRestoreInstanceState2")
    }

    override fun getLastNonConfigurationInstance(): Any? {
        logD(TAG, "$TAG getLastNonConfigurationInstance")
        return super.getLastNonConfigurationInstance()
    }
}