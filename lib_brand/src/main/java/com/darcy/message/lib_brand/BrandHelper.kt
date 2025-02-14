package com.darcy.message.lib_brand

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import com.darcy.message.lib_brand.util.BrandNameUtil.isOppo
import com.darcy.message.lib_brand.util.BrandNameUtil.isVivo
import com.darcy.message.lib_brand.util.BrandNameUtil.isXiaoMi
import java.lang.reflect.Method


object BrandHelper {

    private val TAG = BrandHelper::class.java.simpleName

    private fun checkManufacturer(manufacturer: String): Boolean {
        return manufacturer.equals(Build.MANUFACTURER, true)
    }

    fun isBackgroundStartAllowed(context: Context): Boolean {
        if (isXiaoMi()) {
            return isXiaomiBgStartPermissionAllowed(context)
        }

        if (isVivo()) {
            return isVivoBgStartPermissionAllowed(context)
        }

        if (isOppo()) {
            return Settings.canDrawOverlays(context)
        }
        return true
    }


    private fun isXiaomiBgStartPermissionAllowed(context: Context): Boolean {
        val ops = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        try {
            val op = 10021
            val method: Method = ops.javaClass.getMethod(
                "checkOpNoThrow",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                String::class.java
            )
            val result =
                method.invoke(ops, op, android.os.Process.myUid(), context.packageName) as Int
            return result == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun isVivoBgStartPermissionAllowed(context: Context): Boolean {
        return getVivoBgStartPermissionStatus(context) == 0
    }

    /**
     * 判断Vivo后台弹出界面状态， 1无权限，0有权限
     * @param context context
     */
    @SuppressLint("Range")
    private fun getVivoBgStartPermissionStatus(context: Context): Int {
        val uri: Uri =
            Uri.parse("content://com.vivo.permissionmanager.provider.permission/start_bg_activity")
        val selection = "pkgname = ?"
        val selectionArgs = arrayOf(context.packageName)
        var state = 1
        try {
            context.contentResolver.query(uri, null, selection, selectionArgs, null)?.use {
                if (it.moveToFirst()) {
                    state = it.getInt(it.getColumnIndex("currentstate"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return state
    }

    /**
     * 判断小米锁屏显示状态
     * true:开启 false:未开启
     */
    fun isXiaomiShowLockViewAllowed(context: Context): Boolean {
        try {
            val ops: AppOpsManager =
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val op = 10020 // >= 23
            // ops.checkOpNoThrow(op, uid, packageName)
            val method = ops!!.javaClass.getMethod(
                "checkOpNoThrow", *arrayOf<Class<*>?>(
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    String::class.java
                )
            )
            val result = method.invoke(ops, op, Process.myUid(), context.packageName) as Int

            return result == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 判断小米锁屏显示状态
     * true:开启 false:未开启
     */
    fun isVivoShowLockViewAllowed(context: Context): Boolean {
        return getVivoLockStatus(context) == 0
    }

    /**
     * 判断vivo锁屏显示 1未开启 0开启
     * @param context
     * @return
     */
    @SuppressLint("Range")
    fun getVivoLockStatus(context: Context): Int {
        val packageName = context.packageName
        val uri2 =
            Uri.parse("content://com.vivo.permissionmanager.provider.permission/control_locked_screen_action")
        val selection = "pkgname = ?"
        val selectionArgs = arrayOf(packageName)
        try {
            val cursor = context.contentResolver
                .query(uri2, null, selection, selectionArgs, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val currentMode = cursor.getInt(cursor.getColumnIndex("currentstate"))
                    cursor.close()
                    return currentMode
                } else {
                    cursor.close()
                    return 1
                }
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
        return 1
    }

}
