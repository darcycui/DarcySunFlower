package com.darcy.message.lib_camera.camera1.activity

import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.message.lib_camera.camera1.CameraBackgroundHelper
import com.darcy.message.lib_camera.databinding.LibCameraActivityInvisibleCameraBinding
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * 透明Activity实现偷偷拍照
 */
class InVisibleCameraActivity : AppCompatActivity() {
    private val binding: LibCameraActivityInvisibleCameraBinding by lazy {
        LibCameraActivityInvisibleCameraBinding.inflate(layoutInflater)
    }
    private val context: Context by lazy {
        this
    }

    private val outputMediaFile: File?
        get() {
            val mediaStorageDir = File(getExternalFilesDir(null), "MyCameraAppInvisible")
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
                logI("activity获取照片成功" + outputMediaFile?.absolutePath)
                Toast.makeText(context, "activity获取照片成功", Toast.LENGTH_SHORT).show()
                finish() // 拍照成功，结束Activity
            } catch (e: IOException) {
                logE("pictureCallback save error")
                logE("activity获取照片失败")
                Toast.makeText(context, "activity获取照片失败", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } finally {
                camera.stopPreview()
                camera.release()
            }
        }
    }

    private val mainScope: CoroutineScope by lazy {
        MainScope()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.lib_camera_activity_camera)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 设置 Activity 的大小为 1 像素
        val params = window.attributes
        params.width = 1
        params.height = 1
        params.flags =
            params.flags or (WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        window.attributes = params

        // 延时拍照
        autoTakePictureAfterDelay()
    }

    private fun autoTakePictureAfterDelay() {
        mainScope.launch {
            logI("autoTakePictureAfterDelay start")
            repeat(1) {
                delay(1_000)
                logD("delay $it")

            }
//            mCamera?.takePicture(null, null, pictureCallback)
            CameraBackgroundHelper.takePicture(context, 0, outputMediaFile!!, pictureCallback)
            logI("autoTakePictureAfterDelay end")
        }
    }

}
