package com.darcy.message.lib_camera.camera1.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.widget.Toast
import com.darcy.message.lib_camera.camera1.CameraBackgroundHelper
import com.darcy.message.lib_camera.camera1.activity.InVisibleCameraActivity
import com.darcy.message.lib_camera.camera1.service.BackgroundCameraService
import com.darcy.message.lib_camera.camera1.service.ForegroundCameraService
import com.darcy.message.lib_camera.camera1.service.WindowManagerCameraService
import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logW
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CaptureReceiver : BroadcastReceiver() {
    private val context: Context by lazy { AppHelper.getAppContext() }

    private val outputMediaFile: File?
        get() {
            val mediaStorageDir = File(context.getExternalFilesDir(null), "MyCameraAppReceiver")
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }
            val timeStamp = System.currentTimeMillis().toString()
            return File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
        }

    private val pictureCallback: PictureCallback = object : PictureCallback {
        override fun onPictureTaken(data: ByteArray, camera: Camera) {
            logI("pictureCallback save start")
            val pictureFile: File = outputMediaFile ?: return
            try {
                val fos = FileOutputStream(pictureFile)
                fos.write(data)
                fos.close()
                logI("pictureCallback save end")
                logI("receiver获取照片成功" + outputMediaFile?.absolutePath)
                Toast.makeText(context, "receiver获取照片成功", Toast.LENGTH_SHORT).show()
                // 拍照成功，结束
            } catch (e: IOException) {
                logE("pictureCallback save error")
                logE("receiver获取照片失败")
                Toast.makeText(context, "receiver获取照片失败", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        logD("CaptureReceiver: action-->${intent.action}")
        if (ACTION_CAPTURE == intent.action) {
            logW("CaptureReceiver: 拍照广播收到")
//            startInvisibleActivity(context)
//            startBackgroundService(context)
//            startWindowManagerService(context)
            startForegroundService(context)
//            takePictureInReceiver(context)
        }
    }

    private fun startForegroundService(context: Context) {
        context.startForegroundService(Intent(context, ForegroundCameraService::class.java))
    }

    private fun takePictureInReceiver(context: Context) {
        CameraBackgroundHelper.takePicture(context, 0, outputMediaFile!!, pictureCallback)
    }

    private fun startWindowManagerService(context: Context) {
        context.startService(Intent(context, WindowManagerCameraService::class.java))
    }

    private fun startBackgroundService(context: Context) {
        context.startService(Intent(context, BackgroundCameraService::class.java))
    }

    private fun startInvisibleActivity(context: Context) {
        val targetIntent = Intent(context, InVisibleCameraActivity::class.java)
        targetIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(targetIntent)
    }

    companion object {
        const val ACTION_CAPTURE: String = "action_capture"
    }
}
