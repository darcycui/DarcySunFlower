package com.darcy.message.lib_ui.exts

import android.app.Activity
import android.content.Intent

fun Activity.hasPermission(permissionName: String?): Int {
    return permissionName?.let {
        this.checkSelfPermission(it)
    } ?: run {
        -1
    }
}

fun Activity.startPage(clazz: Class<out Activity>) {
    this.startActivity(Intent(this, clazz))
}