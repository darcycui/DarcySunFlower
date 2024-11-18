package com.darcy.message.lib_app_status.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.print
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object UriUtil {
    private const val APP_FOLDER = "/SunFlower"
    fun insertImageIntoMediaStore(context: Context, file: File): Uri {
        val existUri = checkIfFileExists(context, file.name)
        if (existUri != Uri.EMPTY) {
            return existUri
        }
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + APP_FOLDER
            )
        }

        val uri: Uri? =
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                file.inputStream().copyTo(outputStream)
            }
            logD("MainActivity", "Image inserted into media store: $it")
        }
        return uri ?: Uri.EMPTY
    }

    fun insertVideoIntoMediaStore(context: Context, file: File): Uri {
        val existUri = checkIfFileExists(context, file.name)
        if (existUri != Uri.EMPTY) {
            return existUri
        }
        val contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + APP_FOLDER)
        }

        val uri: Uri? = context.contentResolver.insert(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                file.inputStream().copyTo(outputStream)
            }
            logD("Video inserted into media store: $it")
        }
        return uri ?: Uri.EMPTY
    }

    fun insertAudioIntoMediaStore(context: Context, file: File): Uri {
        val existUri = checkIfFileExists(context, file.name)
        if (existUri != Uri.EMPTY) {
            return existUri
        }
        val contentValues = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg")
            put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_MUSIC)
        }

        val uri: Uri? = context.contentResolver.insert(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                file.inputStream().copyTo(outputStream)
            }
            logD("Audio inserted into media store: $it")
        }
        return uri ?: Uri.EMPTY
    }

    /**
     * 将文件保存到系统的Download文件夹中
     *
     * @param context 上下文
     * @param file 文件
     */
    fun insertFileIntoMediaStore(
        context: Context,
        file: File,
    ): Uri {
        val existUri = checkIfFileExists(context, file.name)
        if (existUri != Uri.EMPTY) {
            return existUri
        }
        val contentResolver = context.contentResolver
        val contentValues = ContentValues()

        // 设置文件元数据
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, file.name)
        contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream")
        contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)

        // 插入MediaStore，获取返回的URI
        val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (uri == null) {
            logE("Failed to insert URI into MediaStore")
            return Uri.EMPTY
        }

        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            // 打开assets目录下的文件
            inputStream = FileInputStream(file)

            // 打开输出流
            outputStream = contentResolver.openOutputStream(uri)

            // 复制文件
            val buffer = ByteArray(1024)
            var length: Int
            while ((inputStream.read(buffer).also { length = it }) > 0) {
                outputStream!!.write(buffer, 0, length)
            }

            // 刷新输出流
            outputStream!!.flush()

            logD("文件已成功保存到: $uri")
        } catch (e: IOException) {
            logE("Error saving file to Download folder:$e")
            e.print()
        } finally {
            // 关闭流
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                logE("Error closing streams :$e")
                e.print()
            }
        }
        return uri
    }

    /**
     * 查询文件是否存在
     */
    fun checkIfFileExists(context: Context, fileName: String): Uri {
        val contentResolver: ContentResolver = context.contentResolver
        val uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Downloads._ID,
            MediaStore.Downloads.DISPLAY_NAME
        )

        val selection = MediaStore.Downloads.DISPLAY_NAME + " = ?"
        val selectionArgs = arrayOf(fileName)

        try {
            contentResolver.query(uri, projection, selection, selectionArgs, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val idColumnIndex = cursor.getColumnIndex(MediaStore.Downloads._ID)
                    val fileId = cursor.getLong(idColumnIndex)
                    logD("文件存在: $fileName, ID: $fileId")

                    // 获取文件的URI
                    val uri = ContentUris.withAppendedId(uri, fileId)
                    return uri
                } else {
                    logD("文件不存在: $fileName")
                }
            }
        } catch (e: Exception) {
            logE("查询文件失败:$e")
            e.print()
        }
        return Uri.EMPTY
    }

}