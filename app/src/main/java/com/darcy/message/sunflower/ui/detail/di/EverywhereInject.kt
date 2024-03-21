package com.darcy.message.sunflower.ui.detail.di

import android.app.Activity
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.sunflower.ui.detail.bean.Parent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

data class EverywhereInject(val id: Int = 100) {
    // @Inject can not be used in non android class
    private lateinit var parent: Parent

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface EverywhereInjectEntryPoint {
        fun parent(): Parent
    }

    fun testParentInject(@ActivityContext context: Activity) {
        val entryPoint = EntryPointAccessors.fromActivity<EverywhereInjectEntryPoint>(context, EverywhereInjectEntryPoint::class.java)
        parent = entryPoint.parent()
        logD(message = "EverywhereInject init parent=$parent")
    }
}