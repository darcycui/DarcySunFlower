package com.darcy.message.lib_camera.camera1.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.hardware.Camera.AutoFocusCallback
import android.hardware.Camera.PictureCallback
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.Toast
import com.darcy.message.lib_camera.CameraPrams
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.print
import com.darcy.message.lib_common.exts.toasts
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class WindowManagerCameraService : Service(), SurfaceHolder.Callback {

    private var windowManager: WindowManager? = null
    private var surfaceView: SurfaceView? = null
    private var camera: Camera? = null

    // 自动对焦回调函数(空实现)
    private val myAutoFocus = AutoFocusCallback { success, camera -> }

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
            }
            logI("service获取照片成功" + outputMediaFile?.absolutePath)
            Toast.makeText(mContext, "service获取照片成功", Toast.LENGTH_SHORT).show()
            releaseCamera(camera)
            stopSelf()
        }

    private val mContext: Context by lazy { this }
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
        // Create new SurfaceView, set its size to 1x1, move it to the top left corner and set this service as a callback
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        addGlobalView()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private fun addGlobalView() {
        if (Settings.canDrawOverlays(this)) {
            toasts("悬浮窗已授权")
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            stopSelf()
            return
        }
        surfaceView = SurfaceView(this)
        surfaceView?.holder?.addCallback(this)
        val params = WindowManager.LayoutParams()
        params.width = 90
        params.height = 160
        params.alpha = 1.0f
//        params.width = 1
//        params.height = 1
//        params.alpha = 0f
        //检查版本，注意当type为TYPE_APPLICATION_OVERLAY时，铺满活动窗口，但在关键的系统窗口下面，如状态栏或IME
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }

        // 屏蔽点击事件
        params.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        windowManager?.addView(surfaceView, params)
    }

    // Method called right after Surface created (initializing and starting MediaRecorder)
    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        initCamera()
    }

    override fun surfaceChanged(
        surfaceHolder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
        startPreview()

        MainScope().launch {
            repeat(5) {
                logD("delay $it")
                delay(1_000)
            }
            // 拍照
            takePicture()
        }
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        if (null != camera) {
            surfaceHolder.removeCallback(this)
            releaseCamera(camera!!)
        }
    }

    // 初始化摄像头
    private fun initCamera() {
        // 如果存在摄像头
        if (checkCameraHardware(mContext!!.applicationContext)) {
            // 获取摄像头（首选前置，无前置选后置）
            if (openCamera()) {
                logI("openCameraSuccess")
            } else {
                logE("openCameraFailed")
            }
        }
    }

    // 对焦并拍照
    private fun takePicture() {
        try {
            // 自动对焦
            camera?.autoFocus(myAutoFocus)
            camera?.takePicture(null, null, myPicCallback)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun startPreview() {
        try {
            // 这里的myCamera为已经初始化的Camera对象
            camera?.let {
                val parameters = it.parameters
                parameters.setPictureSize(CameraPrams.previewWidth, CameraPrams.previewHeight)
                // 设置自动对焦模式
                parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                it.parameters = parameters
                it.setPreviewDisplay(surfaceView!!.holder)
                it.startPreview()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            camera?.let { releaseCamera(it) }
        }
    }

    // 判断是否存在摄像头
    private fun checkCameraHardware(context: Context): Boolean {
        return if (context.packageManager.hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY
            )
        ) {
            // 设备存在摄像头
            true
        } else {
            // 设备不存在摄像头
            false
        }
    }

    // 得到后置摄像头
    private fun openCamera(): Boolean {
        // 尝试开启前置摄像头
        try {
            camera = Camera.open(0)
            camera?.setDisplayOrientation(90)
            return true
        } catch (e: Exception) {
            logE("openCameraFailed:${e.message}")
            e.print()
        }
        return false
    }

    private fun releaseCamera(camera: Camera?) {
        camera?.let {
            it.stopPreview()
            it.release()
            this.camera = null
        }
    }

    // Stop recording and remove SurfaceView
    override fun onDestroy() {
        surfaceView?.let {
            windowManager?.removeView(surfaceView)
        }
        camera?.let { releaseCamera(it) }
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
