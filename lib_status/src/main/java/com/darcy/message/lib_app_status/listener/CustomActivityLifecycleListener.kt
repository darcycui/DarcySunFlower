package com.darcy.message.lib_app_status.listener

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.darcy.message.lib_app_status.utils.ActivityUtil
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logW

object CustomActivityLifecycleListener : Application.ActivityLifecycleCallbacks {
    private val TAG: String = "MyActivityLifecycleListener"
    private var activityCount = 0
    fun isBackground(): Boolean {
        return activityCount <= 0
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        logD("onActivityCreated: ${activity.componentName}")
    }

    override fun onActivityStarted(activity: Activity) {
        activityCount++
        logI("onActivityStarted: ${activity.componentName} activityCount=$activityCount")
    }

    override fun onActivityResumed(activity: Activity) {
        logD("onActivityResumed: ${activity.componentName}")
    }

    override fun onActivityPaused(activity: Activity) {
        logD("onActivityPaused: ${activity.componentName}")
    }

    override fun onActivityStopped(activity: Activity) {
        activityCount--
        logW("onActivityStopped: ${activity.componentName} activityCount=$activityCount")
        if (activityCount <= 0) {
            ActivityUtil.getTopActivityName(activity).also {
                logW("栈顶Activity:$it")
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        logD("onActivitySaveInstanceState: ${activity.componentName}")
    }

    override fun onActivityDestroyed(activity: Activity) {
        logE("onActivityDestroyed: ${activity.componentName}")
    }
}