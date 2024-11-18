package com.darcy.message.lib_camera.camera1.activity

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.message.lib_camera.camera1.receiver.CaptureReceiver
import com.darcy.message.lib_camera.databinding.LibCameraActivityTestCameraReceiverBinding
import com.darcy.message.lib_common.exts.print
import com.darcy.message.lib_common.exts.toasts

class TestCameraReceiverActivity : AppCompatActivity() {
    private val binding: LibCameraActivityTestCameraReceiverBinding by lazy {
        LibCameraActivityTestCameraReceiverBinding.inflate(layoutInflater)
    }
    private val context: Context by lazy {
        this
    }
    private val captureReceiver: CaptureReceiver by lazy {
        CaptureReceiver()
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
    }

    private fun initListener() {
        binding.btnRegister.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(
                    captureReceiver,
                    IntentFilter(CaptureReceiver.ACTION_CAPTURE),
                    Context.RECEIVER_EXPORTED
//                    Context.RECEIVER_NOT_EXPORTED
                )
                toasts("注册广播")
            } else {
                registerReceiver(captureReceiver, IntentFilter(CaptureReceiver.ACTION_CAPTURE))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(captureReceiver)
        } catch (e: Exception) {
            e.print()
        }
    }
}