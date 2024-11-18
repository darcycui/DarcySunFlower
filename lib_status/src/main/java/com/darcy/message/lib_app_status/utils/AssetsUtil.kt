package com.darcy.message.lib_app_status.utils

import android.content.Context
import android.content.res.AssetManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object AssetsUtil {
    /**
     * 将assets目录下的文件复制到外部存储卡
     */
    fun copyAssetToMediaFolder(context: Context, assetName: String): File {
        val assetManager: AssetManager = context.assets
        val inputStream: InputStream
        try {
            inputStream = assetManager.open(assetName)
            val file = File(context.getExternalFilesDir("media"), assetName)
            val outputStream: OutputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()

            return file
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return File("")
    }
}