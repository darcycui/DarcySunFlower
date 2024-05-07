package com.darcy.message.lib_ui.base.lifecycle

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logV

open class FragmentLifecycle : Fragment() {
    protected val TAG: String by lazy {
        this::class.java.simpleName
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        logV(TAG, "$TAG onAttach ${hashCode()}")
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

    override fun onDetach() {
        super.onDetach()
        logE(TAG, "$TAG onDetach")
    }
}