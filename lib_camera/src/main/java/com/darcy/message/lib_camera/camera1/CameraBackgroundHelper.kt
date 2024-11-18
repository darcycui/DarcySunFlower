package com.darcy.message.lib_camera.camera1

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.print
import java.io.File

object CameraBackgroundHelper {

    // 自动对焦回调函数(空实现)
    private val myAutoFocus = Camera.AutoFocusCallback { success, camera -> }
    fun takePicture(context: Context, cameraId: Int, file: File, callback: Camera.PictureCallback) {
        try {
            logD("takePicture start")
            val count = Camera.getNumberOfCameras()
            if(count <= 0 || cameraId >= count){
                logE("takePicture cameraId not available:count=$count cameraId=$cameraId")
                return
            }
            val open = Camera.open(cameraId)
            val surfaceTexture = SurfaceTexture(0)
            open.setPreviewTexture(surfaceTexture)
            open.startPreview()
            open.autoFocus(myAutoFocus)
            open.takePicture(null, null, callback)
            logI("takePicture end")
        } catch (e: Exception) {
            logE("takePicture error: ${e.message}")
            e.print()
        }
    }
}