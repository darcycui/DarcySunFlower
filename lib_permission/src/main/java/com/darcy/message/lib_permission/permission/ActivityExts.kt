package com.darcy.message.lib_permission.permission

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates

private val nextLocalRequestCode = AtomicInteger()

private val nextKey: String
    get() = "activity_rq#${nextLocalRequestCode.getAndIncrement()}"

/**
 * request permission by ActivityResultLauncher
 */
fun ComponentActivity.requestPermissionsByLauncher(
    permissions: Array<String>,
    onGranted: () -> Unit,
    onDenied: (shouldShowRequestPermissionRationale: Boolean) -> Unit
) {
    var hasPermissions = true
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            hasPermissions = false
            break
        }
    }
    if (hasPermissions) {
        onGranted()
        return
    }
    var launcher by Delegates.notNull<ActivityResultLauncher<Array<String>>>()
    // 注册一个ActivityResultLauncher 注意这里使用register绕过生命周期检查
    // https://mp.weixin.qq.com/s/-8EScdoY803MHXze8x4bwg
    launcher = this.activityResultRegistry.register(
        nextKey,
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        var allAllow = true
        for (allow in result.values) {
            if (!allow) {
                allAllow = false
                break
            }
        }
        if (allAllow) {
            onGranted()
        } else {
            var shouldShowCustomRequest = false
            for (permission in permissions) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    shouldShowCustomRequest = true
                    break
                }
            }
            onDenied(shouldShowCustomRequest)
        }
        launcher.unregister()
    }
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                launcher.unregister()
                lifecycle.removeObserver(this)
            }
        }
    })
    launcher.launch(permissions)
}


/**
 * open the app details page
 */
fun Activity.toAppSettingsDetail(requestCode: Int) {
    val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", this.packageName, null)
    intent.setData(uri)
    this.startActivityForResult(intent, requestCode)
}