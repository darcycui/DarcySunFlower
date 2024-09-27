package com.darcy.message.lib_ui.base.lifecycle

import android.content.Context
import android.content.res.Configuration
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
    protected val context: Context by lazy {
        this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI(TAG, "$TAG onCreate")
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        logI(TAG, "$TAG onCreate2")
    }

    override fun onStart() {
        super.onStart()
        logD(TAG, "$TAG onStart")
    }

    override fun onResume() {
        super.onResume()
        logD(TAG, "$TAG onResume")
    }

    override fun onPause() {
        super.onPause()
        logD(TAG, "$TAG onPause")
    }

    override fun onStop() {
        super.onStop()
        logD(TAG, "$TAG onStop")
    }

    override fun onRestart() {
        super.onRestart()
        logD(TAG, "$TAG onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        logE(TAG, "$TAG onDestroy")
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        logD(TAG, "$TAG onConfigurationChanged")
    }

    override fun getLastNonConfigurationInstance(): Any? {
        logD(TAG, "$TAG getLastNonConfigurationInstance")
        return super.getLastNonConfigurationInstance()
    }
}