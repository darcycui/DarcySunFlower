package com.darcy.message.lib_brand.util

import android.os.Build

object BrandNameUtil {
    fun isXiaoMi(): Boolean {
        return checkManufacturer("xiaomi")
    }

    fun isOppo(): Boolean {
        return checkManufacturer("oppo")
    }

    fun isVivo(): Boolean {
        return checkManufacturer("vivo")
    }

    private fun checkManufacturer(manufacturer: String): Boolean {
        return manufacturer.equals(Build.MANUFACTURER, true)
    }

}