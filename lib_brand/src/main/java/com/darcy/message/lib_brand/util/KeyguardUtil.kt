package com.darcy.message.lib_brand.util

import android.app.KeyguardManager
import android.content.Context

object KeyguardUtil {
    fun isKeyguardLocked(context: Context): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isKeyguardLocked
    }
}