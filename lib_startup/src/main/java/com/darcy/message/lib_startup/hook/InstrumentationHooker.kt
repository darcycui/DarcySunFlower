package com.darcy.message.lib_startup.hook

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.print
import java.lang.reflect.Field
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy


/**
 * Hook Instrumentation to start activity
 */
object InstrumentationHooker {

    fun hookInstrumentation(context: Context) {
        try {
            logD("InstrumentationHooker start")
            val field: Field
            val iActivityManager: Class<*>

            // 1 获取Instrumentation中调用startActivity(intent)方法的对象 taskManagerSingleton
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // 10.0以上是ActivityTaskManager中的IActivityTaskManagerSingleton
                val activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager")
                field =
                    activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton")
                iActivityManager = Class.forName("android.app.IActivityTaskManager")
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 8.0,9.0在ActivityManager类中IActivityManagerSingleton
                val activityManagerClass: Class<*> = ActivityManager::class.java
                field = activityManagerClass.getDeclaredField("IActivityManagerSingleton")
                iActivityManager = Class.forName("android.app.IActivityManager")
            } else {
                // 8.0以下在ActivityManagerNative类中 gDefault
                val activityManagerNative = Class.forName("android.app.ActivityManagerNative")
                field = activityManagerNative.getDeclaredField("gDefault")
                iActivityManager = Class.forName("android.app.IActivityManager")
            }
            field.isAccessible = true
            val taskManagerSingleton: Any = field.get(null)

            // 2 获取 taskManagerSingleton 中的原mInstance 也就是要代理的对象
            val singletonClass = Class.forName("android.util.Singleton")
            val mInstanceField: Field = singletonClass.getDeclaredField("mInstance")
            mInstanceField.isAccessible = true
            val getMethod = singletonClass.getDeclaredMethod("get")
            val mInstance = getMethod.invoke(taskManagerSingleton) ?: return

            // 3 创建代理proxy
            val proxy: Any = Proxy.newProxyInstance(
                Thread.currentThread().contextClassLoader,
                arrayOf<Class<*>>(iActivityManager),
                AmsHookBinderInvocationHandler(context, mInstance)
            )
            // 4 替换
            mInstanceField.set(taskManagerSingleton, proxy)
        } catch (e: Exception) {
            logE("InstrumentationHooker ERROR")
            e.print()
        }
        logD("InstrumentationHooker SUCCESS")
    }

}

//动态代理执行类
class AmsHookBinderInvocationHandler(private val context: Context,private val obj: Any) : InvocationHandler {
    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any {
        if ("startActivity" == method.name) {
            val raw: Intent
            var index = 0
            for (i in args.indices) {
                if (args[i] is Intent) {
                    index = i
                    break
                }
            }
            if (index == 0) {
                logE("startActivity参数异常")
                return method.invoke(obj, *args)
            }

            //原始意图
            raw = args[index] as Intent
            logD("原始意图：$raw")

            //设置新的Intent-直接制定LoginActivity
            val newIntent = Intent()
            val targetPackage = context.packageName
            val componentName =
                ComponentName(targetPackage, "com.darcy.message.sunflower.MainActivity")
            newIntent.setComponent(componentName)
            logD("新的Intent: $newIntent")

            logI("改变了Activity启动")
            args[index] = newIntent

            return method.invoke(obj, *args)
        }

        //如果不是拦截的startActivity方法，就直接放行
        return method.invoke(obj, *args)
    }
}