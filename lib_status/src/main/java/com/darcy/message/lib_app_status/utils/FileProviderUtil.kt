package com.darcy.message.lib_app_status.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.exts.logI
import java.io.File

object FileProviderUtil {
    val APPLICATION_ID = AppHelper.getAppContext().packageName
    fun getFileUriByFileProvider(context: Context, file: File): Uri {
        // fileProvider 授权file 访问权限
        return FileProvider.getUriForFile(context, "$APPLICATION_ID.fileprovider", file)
    }

    fun previewFileByProvider(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(getFileUriByFileProvider(context, file), getFileMimeType(file.absolutePath))
        if(intent.resolveActivity(context.packageManager) == null){
            Toast.makeText(context, "can not preview this file", Toast.LENGTH_SHORT).show()
            return
        }
        context.startActivity(intent)
    }

    private fun getFileMimeType(filePath: String): String {
        val fileExt = filePath.split(".").lastOrNull() ?: ""
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExt)
        logI("FilePreviewHelper: fileExt=$fileExt mimeType=$mimeType")
        return mimeType ?: "*/*"
    }
}