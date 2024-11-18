package com.darcy.message.lib_app_status.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.darcy.message.lib_common.exts.logE

object FilePreviewUtil {

    fun previewImage(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "image/*")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        // 检查是否有activity响应该intent
        val resolveInfoList = context.packageManager.queryIntentActivities(intent, 0)
        if (resolveInfoList.isEmpty()) {
            logE("No activity found to open image")
            return
        }
        context.startActivity(intent)
    }

    fun previewVideo(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "video/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }

    fun previewAudio(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "audio/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }
    fun previewFile(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "text/plain")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }
}