package com.darcy.message.lib_camera.camera1.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.message.lib_camera.camera1.CameraBackgroundHelper
import com.darcy.message.lib_camera.camera1.service.CameraService
import com.darcy.message.lib_camera.databinding.LibCameraActivityTestCameraBackgroundBinding
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.toasts
import com.darcy.message.lib_permission.permission.PermissionUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class TestCameraBackgroundActivity : AppCompatActivity() {
    private val binding: LibCameraActivityTestCameraBackgroundBinding by lazy {
        LibCameraActivityTestCameraBackgroundBinding.inflate(layoutInflater)
    }
    private val context: Context by lazy {
        this
    }

    private val outputMediaFile: File?
        get() {
            val mediaStorageDir = File(getExternalFilesDir(null), "MyCameraAppBackground")
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }
            val timeStamp = System.currentTimeMillis().toString()
            return File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
        }

    private val maiScope: CoroutineScope = MainScope()

    override fun onDestroy() {
        super.onDestroy()
        maiScope.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initListener()
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (PermissionUtil.checkPermissions(
                context, listOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.FOREGROUND_SERVICE_CAMERA,
                )
            )
        ) {
            toasts("相机已授权")
        } else {
            PermissionUtil.requestPermissions(this, listOf(android.Manifest.permission.CAMERA),
                onGranted = {
                    toasts("相机已授权")
                },
                onDenied = {
                    // 处理拒绝授权的情况
                })
        }
        if (Settings.canDrawOverlays(this)) {
            toasts("悬浮窗已授权")
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            startActivityForResult(intent, 100)
        }
    }

    private fun initListener() {
        binding.btnCapture1.setOnClickListener {
            maiScope.launch {
                logD("delay start")
                repeat(10) {
                    logD("delay: $it")
                    delay(1000)
                }
                logD("delay end")
                CameraBackgroundHelper.takePicture(context, 0, outputMediaFile!!) { data, open ->
                    logD("takePicture: save start")
                    val pictureFile: File = outputMediaFile ?: return@takePicture
                    try {
                        val fos = FileOutputStream(pictureFile)
                        fos.write(data)
                        fos.close()
                        logD("takePicture: save end")
                        open.stopPreview()
                        open.release()
                    } catch (e: IOException) {
                        logE("takePicture: save error")
                        e.printStackTrace()
                    }
                }
            }
        }
        binding.btnCapture2.setOnClickListener {
            CameraBackgroundHelper.takePicture(context, 1, outputMediaFile!!) { data, open ->
                logD("takePicture: save start")
                val pictureFile: File = outputMediaFile ?: return@takePicture
                try {
                    val fos = FileOutputStream(pictureFile)
                    fos.write(data)
                    fos.close()
                    logD("takePicture: save end")
                    open.stopPreview()
                    open.release()
                } catch (e: IOException) {
                    logE("takePicture: save error")
                    e.printStackTrace()
                }
            }
        }
        binding.btnCaptureInvisibleActivity.setOnClickListener {
            startActivity(Intent(context, InVisibleCameraActivity::class.java))
        }
        binding.btnCaptureService.setOnClickListener {
            startService(Intent(context, CameraService::class.java))
        }
    }
}