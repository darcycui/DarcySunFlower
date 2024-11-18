package com.darcy.message.lib_app_status.utils

import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import android.provider.Settings
import com.darcy.message.lib_common.exts.logD


object ActivityUtil {
    fun getTopActivityName(context: Context): String? {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        if (activityManager != null) {
            try {
                val topTask = activityManager.getRunningTasks(1)[0]
                if (topTask?.topActivity != null) {
                    val result = topTask.topActivity?.flattenToShortString()
                    return if (result.isNullOrEmpty() || "/" == result) {
                        getTopActivityNameWithUsageStats(context)
                    } else {
                        result
                    }
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getTopActivityNameWithUsageStats(context: Context): String? {
        if (!hasUsageStatsPermission(context)) {
            requestUsageStatsPermission(context)
            return null
        }
        logD("已授权最近使用app权限")
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            time - 1000 * 10,
            time
        )
        if (stats != null && stats.isNotEmpty()) {
            var recentStats: UsageStats? = null
            for (usageStats in stats) {
                if (recentStats == null || recentStats.lastTimeUsed < usageStats.lastTimeUsed) {
                    recentStats = usageStats
                }
            }
            if (recentStats != null) {
                return recentStats.packageName
            }
        }
        return null
    }

    // 如果没有权限，引导用户去开启权限设置
    fun requestUsageStatsPermission(context: Context) {
        val intent: Intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        context.startActivity(intent)
    }

    //检测用户是否对本app开启了“Apps with usage access”权限
    private fun hasUsageStatsPermission(context: Context): Boolean {
        val appOps =
            context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), context.packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

}