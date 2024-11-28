package com.darcy.message.lib_repackage.signature.hook

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.Signature
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.print
import java.lang.reflect.Field
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Hook PackageManager to provide fake signature for app
 */
object PackageManagerHooker {
    fun hookPackageManager(context: Context) {
        try {
            logD("PackageManagerHook start")
            // 获取原sPackageManager
            val clzActivityThread = Class.forName("android.app.ActivityThread")
            val methodGetPackageManager = clzActivityThread.getDeclaredMethod("getPackageManager")
            methodGetPackageManager.isAccessible = true
            val sPackageManager = methodGetPackageManager.invoke(null)

            // 动态代理创建 hookProxy
            val clzIPackageManager = Class.forName("android.content.pm.IPackageManager")
            val hookProxy: Any = Proxy.newProxyInstance(
                clzActivityThread.classLoader,
                arrayOf<Class<*>>(clzIPackageManager),
                PackageManagerProxy(context, sPackageManager)
            )

            // 替换原sPackageManager
            val filedIPackageManager: Field = clzActivityThread.getDeclaredField("sPackageManager")
            filedIPackageManager.isAccessible = true
            filedIPackageManager.set(null, hookProxy)
        } catch (e: Exception) {
            logE("PackageManagerHook Error")
            e.print()
        }
        logI("PackageManagerHook SUCCESS")
    }
}

class PackageManagerProxy(private val context: Context, private val realPackageManager: Any) :
    InvocationHandler {
        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        if (proxy == null || method == null || args == null) {
            return null
        }
        if (method.name.equals("getPackageInfo")) {
            var packageName = ""
            if (args.isNotEmpty() && args[0] is String) {
                packageName = args[0] as String
            }
            // 注意这里array转不定参数 使用*
            val packageInfo: PackageInfo = method.invoke(realPackageManager, *args) as PackageInfo
            if (context.packageName.equals(packageName)) {
                // 原APK签名
                val fakeByte: ByteArray = byteArrayOf(50,51,52,53,54,55,56,57,58,59,50,51,52,53,54,55,56,57,58,59)
                val fakeSignature: Signature = Signature(fakeByte)
                if (packageInfo.signatures == null || packageInfo.signatures.isEmpty()) {
                    packageInfo.signatures = arrayOfNulls(1)
                }
                packageInfo.signatures[0] = fakeSignature
                return packageInfo
            }
        }
        logD("PackageManagerHook", "invoke: ${method.name}")
        return method.invoke(realPackageManager, *args)
    }

}