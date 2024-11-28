package com.darcy.message.lib_repackage.dex

import android.content.Context
import com.darcy.message.lib_common.exts.print
import java.util.zip.ZipFile


object DexHelper {
    fun getApkCRC(context: Context): Long {
        val zf: ZipFile
        try {
            zf = ZipFile(context.packageCodePath)
            // 获取apk安装后的路径
            val ze = zf.getEntry("classes.dex")
            return ze.crc
        } catch (e: Exception) {
            e.print()
            return 0
        }
    }
}