package com.darcy.message.lib_permission.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

object PermissionUtil {
    fun checkPermissions(
        context: Context,
        permissions: List<String>,
    ): Boolean {
        // 检查权限 是否被授予
        val granted = permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
        return granted
    }

    fun requestPermissions(
        context: ComponentActivity,
        permissions: List<String>,
        onGranted: () -> Unit,
        onDenied: (shouldShowRequestPermissionRationale: Boolean) -> Unit
    ) {
        if (checkPermissions(context, permissions)) {
            onGranted()
            return
        }
        // use ActivityResultLauncher to request permission
        context.requestPermissionsByLauncher(permissions.toTypedArray(), onGranted, onDenied)
    }
}

