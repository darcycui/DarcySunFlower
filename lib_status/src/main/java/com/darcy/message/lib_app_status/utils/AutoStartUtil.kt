package com.darcy.message.lib_app_status.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.darcy.message.lib_common.exts.print


object AutoStartUtil {
    fun openSettingsPage(context: Context) {
        // 判断是华为机型
        val brand = Build.BRAND
        if (brand.equals("HUAWEI", ignoreCase = true)) {
            val intent = Intent()
            // 对于大多数华为设备
            intent.setComponent(
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity"
                )
            )
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                e.print()
                try {
                    // 如果上述方式不工作，试试这个：
                    intent.setComponent(
                        ComponentName(
                            "com.huawei.systemmanager",
                            "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity"
                        )
                    )
                    // fixme 权限错误: requires com.huawei.permission.external_app_settings.USE_COMPONENT
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.print()
                    // 如果无法打开指定的应用程序，则尝试打开一般的应用管理界面
//                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS")
//                    val uri = Uri.fromParts("package", context.packageName, null)
//                    intent.setData(uri)
//                    context.startActivity(intent)
                }
            }

        }
    }

}
