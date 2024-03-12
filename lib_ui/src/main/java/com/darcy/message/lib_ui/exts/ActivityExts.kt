package com.darcy.message.lib_ui.exts

import android.app.Activity
import android.content.Intent

fun Activity.hasPermission(permissionName: String?) : Int {
    permissionName?.let {
        return this.checkSelfPermission(it)
    } ?: run {
        return -1
    }
}

fun Activity.startPage(clazz: Class<out Activity>) {
    this.startActivity(Intent(this, clazz))
}