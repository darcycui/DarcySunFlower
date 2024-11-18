package com.darcy.message.lib_app_status.utils

import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings


object DeveloperModeUtil {
    fun isDeveloperMode(context: Context): Boolean {
        // 检测开发者模式是否打开
        return try {
            val contentResolver = context.contentResolver
            val developerOptionsStatus: Int = Settings.Secure.getInt(
                contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                0
            )
            developerOptionsStatus != 0
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}