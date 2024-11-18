package com.darcy.message.lib_camera.camera1.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.os.IBinder
import android.widget.Toast
import com.darcy.message.lib_camera.camera1.CameraBackgroundHelper
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class BackgroundCameraService : Service(){

    // 拍照成功回调函数
    private val myPicCallback =
        PictureCallback { data, camera -> // 完成拍照后关闭Activity
            // 将得到的照片进行270°旋转，使其竖直
            var bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            val matrix = Matrix()
            matrix.preRotate(90f)
            bitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width,
                bitmap.height, matrix, true
            )
            // 创建并保存图片文件
            try {
                val fos = FileOutputStream(outputMediaFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos)
                fos.close()
            } catch (error: Exception) {
                Toast.makeText(mContext, "service保存照片失败", Toast.LENGTH_SHORT)
                    .show()
                logE("service保存照片失败$error")
                error.printStackTrace()
                releaseCamera(camera)
                stopSelf()
            }
            logI("service获取照片成功" + outputMediaFile?.absolutePath)
            Toast.makeText(mContext, "service获取照片成功", Toast.LENGTH_SHORT).show()
            releaseCamera(camera)
            stopSelf()
        }

    private var mContext: Context? = null
    private val outputMediaFile: File?
        get() {
            val mediaStorageDir = File(getExternalFilesDir(null), "MyCameraAppService")
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }
            val timeStamp = System.currentTimeMillis().toString()
            return File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
        }

    override fun onCreate() {
        mContext = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MainScope().launch {
            repeat(5) {
                logD("delay $it")
                delay(1_000)
            }
            CameraBackgroundHelper.takePicture(mContext!!, 1, outputMediaFile!!, myPicCallback)
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun releaseCamera(camera: Camera?) {
        camera?.let {
            it.stopPreview()
            it.release()
        }
    }

    // Stop recording and remove SurfaceView
    override fun onDestroy() {
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
