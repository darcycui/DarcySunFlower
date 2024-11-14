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
import android.hardware.Camera.CameraInfo
import android.hardware.Camera.PictureCallback
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.Toast
import com.darcy.message.lib_camera.CameraPrams
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CameraService : Service(), SurfaceHolder.Callback {

    private var windowManager: WindowManager? = null
    private var surfaceView: SurfaceView? = null
    private var camera: Camera? = null

    var TAG: String = "CameraService"
    var mContext: Context? = null
    private var mParameters: Camera.Parameters? = null
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

        // Create new SurfaceView, set its size to 1x1, move it to the top left corner and set this service as a callback
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        surfaceView = SurfaceView(this)
        val params = WindowManager.LayoutParams()
        params.width = 1
        params.height = 1
        params.alpha = 0f
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

        windowManager!!.addView(surfaceView, params)
        surfaceView!!.holder.addCallback(this)
    }

    // Method called right after Surface created (initializing and starting MediaRecorder)
    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        /* camera = Camera.open();
                camera.unlock();*/

        initCamera()
    }

    // 初始化摄像头
    private fun initCamera() {
        // 如果存在摄像头

        if (checkCameraHardware(mContext!!.applicationContext)) {
            // 获取摄像头（首选前置，无前置选后置）
            if (openFacingFrontCamera()) {
                Log.i(TAG, "openCameraSuccess")
                // 进行对焦
                autoFocus()
            } else {
                Log.i(TAG, "openCameraFailed")
            }
        }
    }

    // 对焦并拍照
    private fun autoFocus() {
        try {
            // 因为开启摄像头需要时间，这里让线程睡两秒
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        startPreview()
        // 自动对焦
        camera!!.autoFocus(myAutoFocus)


        // 对焦后拍照
        //  camera.unlock();
        camera!!.takePicture(null, null, myPicCallback)
    }

    private fun startPreview() {
        setCameraParameters()
        camera!!.startPreview()
    }

    /**
     * 设置Camera参数
     * 设置预览界面的宽高，图片保存的宽、高
     */
    private fun setCameraParameters() {
        if (camera != null) {
            Log.d(TAG, "setCameraParameters >> begin. mCamera != null")
            if (mParameters == null) {
                mParameters = camera!!.parameters
            }

            var PreviewWidth = 0
            var PreviewHeight = 0
            val sizeList = mParameters!!.supportedPreviewSizes
            if (sizeList.size > 1) {
                val itor: Iterator<Camera.Size> = sizeList.iterator()
                while (itor.hasNext()) {
                    val cur = itor.next()
                    if (cur.width >= PreviewWidth
                        && cur.height >= PreviewHeight
                    ) {
                        PreviewWidth = cur.width
                        PreviewHeight = cur.height
                        break
                    }
                }
            } else if (sizeList.size == 1) {
                val size = sizeList[0]
                PreviewWidth = size.width
                PreviewHeight = size.height
            }
            mParameters!!.setPreviewSize(PreviewWidth, PreviewHeight) //获得摄像区域的大小
            mParameters!!.setPictureSize(PreviewWidth, PreviewHeight) //设置拍出来的屏幕大小


            try {
                camera!!.parameters = mParameters
            } catch (e: Exception) {
                val parameters = camera!!.parameters // 得到摄像头的参数
                camera!!.parameters = parameters
            }
        } else {
            Log.e(TAG, "setCameraParameters >> mCamera == null!!")
        }
    }

    // 判断是否存在摄像头
    private fun checkCameraHardware(context: Context): Boolean {
        return if (context.packageManager.hasSystemFeature(
                PackageManager.FEATURE_CAMERA
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
    private fun openFacingFrontCamera(): Boolean {
        // 尝试开启前置摄像头
        val cameraInfo = CameraInfo()
        camera = Camera.open(0)
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
            camera?.stopPreview()
            camera?.release()
            camera = null
        }
        return true
    }

    // 自动对焦回调函数(空实现)
    private val myAutoFocus = AutoFocusCallback { success, camera -> }

    // 拍照成功回调函数
    private val myPicCallback =
        PictureCallback { data, camera -> // 完成拍照后关闭Activity
            // 将得到的照片进行270°旋转，使其竖直
            var camera = camera
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
                Toast.makeText(mContext, "拍照失败", Toast.LENGTH_SHORT)
                    .show()
                Log.i(TAG, "保存照片失败$error")
                error.printStackTrace()
                camera!!.stopPreview()
                camera.release()
                camera = null
            }

            Log.i(TAG, "获取照片成功" + outputMediaFile?.absolutePath)
            Toast.makeText(mContext, "获取照片成功", Toast.LENGTH_SHORT)
                .show()

            camera!!.stopPreview()
            camera.release()
            camera = null
        }

    // Stop recording and remove SurfaceView
    override fun onDestroy() {
        camera!!.release()

        windowManager!!.removeView(surfaceView)
    }

    override fun surfaceChanged(
        surfaceHolder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
