package com.darcy.message.lib_ui.base.lifecycle

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI(TAG, "$TAG onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logD(TAG, "$TAG onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD(TAG, "$TAG onViewCreated")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        logD(TAG, "$TAG onViewStateRestored")
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

    override fun onDestroyView() {
        super.onDestroyView()
        logD(message = "$TAG onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        logE(TAG, "$TAG onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        logE(TAG, "$TAG onDetach")
    }
}