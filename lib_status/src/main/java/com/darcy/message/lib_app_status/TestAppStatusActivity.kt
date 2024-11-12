package com.darcy.message.lib_app_status

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import com.darcy.message.lib_app_status.databinding.LibStatusActivityTestAppStatusBinding
import com.darcy.message.lib_app_status.receiver.AppRemovedReceiver
import com.darcy.message.lib_app_status.receiver.BatteryStatusReceiver
import com.darcy.message.lib_app_status.receiver.RecentAppsAndHomeKeyReceiver
import com.darcy.message.lib_app_status.receiver.ScreenStateReceiver
import com.darcy.message.lib_app_status.receiver.UserPresentReceiver
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_common.exts.logW
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TestAppStatusActivity : AppCompatActivity() {
    private val TAG = "TestAppStatusActivity"

    private val context: Context by lazy {
        this
    }
    private val binding: LibStatusActivityTestAppStatusBinding by lazy {
        LibStatusActivityTestAppStatusBinding.inflate(layoutInflater)
    }
    private val appRemovedReceiver: AppRemovedReceiver by lazy {
        AppRemovedReceiver()
    }
    private val recentAppsAndHomeKeyReceiver: RecentAppsAndHomeKeyReceiver by lazy {
        RecentAppsAndHomeKeyReceiver()
    }
    private val screenStateReceiver: ScreenStateReceiver by lazy {
        ScreenStateReceiver()
    }
    private val batteryStatusReceiver by lazy {
        BatteryStatusReceiver()
    }
    private val userPresentReceiver by lazy {
        UserPresentReceiver()
    }
    private lateinit var windowInfoTracker: WindowInfoTracker
    private lateinit var windowLayoutInfoFlow: Flow<WindowLayoutInfo>
    private var onBackInvokedCallback: OnBackInvokedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        // edge to edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        registerReceivers()
        initListener()
        handleIntent(intent)
        windowInfoTracker = WindowInfoTracker.getOrCreate(this)
        windowLayoutInfoFlow = windowInfoTracker.windowLayoutInfo(this)
        observeFold()
        resisterPreBack()
    }

    private fun resisterPreBack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedCallback = OnBackInvokedCallback {
                logD("BackGesture:Activity#handleOnBackPressed()")
                finish()
            }
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                onBackInvokedCallback!!
            )

        }
    }

    private fun unregisterPreBack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedCallback?.let {
                onBackInvokedDispatcher.unregisterOnBackInvokedCallback(it)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceivers()
        unregisterPreBack()
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) {
            return
        }
        val action = intent.action
        val data: Uri? = intent.data

        if (Intent.ACTION_VIEW == action && data != null) {
            val scheme = data.scheme
            val host = data.host
            val path = data.path
            logD("AppLink/Scheme: $scheme, Host: $host, Path: $path")
            // 根据传入的数据处理逻辑
            when (path) {
                "/status" -> {
                    logV("deal status")
                }

                else -> {
                    // 默认处理逻辑
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        logV("onWindowFocusChanged: $hasFocus")
        println("onWindowFocusChanged: $hasFocus")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // back键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            logW("onKeyDown: back key")
        }
        // home键(无效)
        if (KeyEvent.KEYCODE_HOME == keyCode) {
            logW("onKeyDown: home key")
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initListener() {
        binding.btnGallery.setOnClickListener {
            // select 10 images from gallery
            val maxNumPhotosAndVideos = 10
            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
            intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, maxNumPhotosAndVideos)
            startActivityForResult(intent, 100)
        }
        binding.btnOtherApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                setClassName("com.example.newhelloworld", "com.example.newhelloworld.MainActivity")
            }
            startActivity(intent)
        }
        binding.btnNotificationListener.setOnClickListener {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(intent)
        }
        binding.btnCrash.setOnClickListener {
            val a = 1 / 0
        }
    }

    private fun registerReceivers() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 注册按键广播
            registerReceiver(
                recentAppsAndHomeKeyReceiver,
                IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS),
                RECEIVER_NOT_EXPORTED
            )

            // 注册卸载广播
            registerReceiver(appRemovedReceiver, IntentFilter().apply {
                addAction(Intent.ACTION_PACKAGE_ADDED)
                addAction(Intent.ACTION_PACKAGE_REPLACED)
                addAction(Intent.ACTION_PACKAGE_REMOVED)
                // 设置scheme
                addDataScheme("package")
            }, RECEIVER_NOT_EXPORTED)

            // 注册屏幕状态广播
            registerReceiver(
                screenStateReceiver, IntentFilter().apply {
                    addAction(Intent.ACTION_SCREEN_ON)
                    addAction(Intent.ACTION_SCREEN_OFF)
                },
                RECEIVER_NOT_EXPORTED
            )
            // 注册充电状态广播
            registerReceiver(
                batteryStatusReceiver,
                IntentFilter(Intent.ACTION_BATTERY_CHANGED),
                RECEIVER_NOT_EXPORTED
            )
            // 注册用户解锁广播
            registerReceiver(
                userPresentReceiver,
                IntentFilter(Intent.ACTION_USER_PRESENT),
                RECEIVER_EXPORTED
            )
        }

    }

    private fun unregisterReceivers() {
        unregisterReceiver(recentAppsAndHomeKeyReceiver)
        unregisterReceiver(appRemovedReceiver)
        unregisterReceiver(screenStateReceiver)
        unregisterReceiver(batteryStatusReceiver)
        unregisterReceiver(userPresentReceiver)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logW("onLowMemory of Activity: delete temp files...")
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig)
        logD("Multi-window mode changed2: $isInMultiWindowMode")
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean) {
        super.onMultiWindowModeChanged(isInMultiWindowMode)
        logD("Multi-window mode changed1: $isInMultiWindowMode")
        // 根据 isInMultiWindowMode 执行相应逻辑
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        logD("Configuration changed: $newConfig")
        checkMultiWindowMode()
        checkPictureInPictureMode()
    }

    private fun checkMultiWindowMode() {
        val isMultiWindow = isInMultiWindowMode
        logD("Is in multi-window mode: $isMultiWindow")
        // 根据 isMultiWindow 执行相应逻辑
    }

    private fun checkPictureInPictureMode() {
        val isPictureInPicture = isInPictureInPictureMode
        logD("Is in picture-in-picture mode: $isPictureInPicture")
        // 根据 isPictureInPicture 执行相应逻辑
    }

    private fun observeFold() {
        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                windowLayoutInfoFlow.collect { layoutInfo ->
                    logI("$TAG:size:${layoutInfo.displayFeatures.size}")
                    val foldingFeature = layoutInfo.displayFeatures
                        .filterIsInstance<FoldingFeature>()
                        .firstOrNull()
                    foldingFeature?.let {
                        logI("$TAG:state:${it.state}")
                    }
                    when {
                        isTableTopPosture(foldingFeature) ->
                            logD("横向半开")

                        isBookPosture(foldingFeature) ->
                            logD("竖向半开")

                        isSeparating(foldingFeature) ->
                            // Dual-screen device
                            foldingFeature?.let {
                                if (it.orientation == FoldingFeature.Orientation.HORIZONTAL) {
                                    logD("横向全展开")
                                } else {
                                    logD("竖向全展开")
                                }
                            }

                        else -> {
                            logD("主屏")
                        }
                    }
                }
            }
        }
    }

    private fun isTableTopPosture(foldFeature: FoldingFeature?): Boolean {
        return foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
                foldFeature.orientation == FoldingFeature.Orientation.HORIZONTAL
    }

    private fun isBookPosture(foldFeature: FoldingFeature?): Boolean {
        return foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
                foldFeature.orientation == FoldingFeature.Orientation.VERTICAL
    }

    private fun isSeparating(foldFeature: FoldingFeature?): Boolean {
        return foldFeature?.state == FoldingFeature.State.FLAT && foldFeature.isSeparating
    }
}