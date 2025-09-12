package com.darcy.message.sunflower

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import com.darcy.lib_flutter.activity.FlutterActivityHelper
import com.darcy.lib_flutter.preload.FlutterPreloadHelper
import com.darcy.message.lib_app_status.TestAppStatusActivity
import com.darcy.message.lib_camera.camera1.activity.TestCameraActivity
import com.darcy.message.lib_camera.camera1.activity.TestCameraBackgroundActivity
import com.darcy.message.lib_camera.camera1.activity.TestCameraReceiverActivity
import com.darcy.message.lib_camera.camera1.service.MessageService
import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_common.arraymap.TestArrayMap
import com.darcy.message.lib_common.arraymap.TestArraySet
import com.darcy.message.lib_common.sparsearray.TestSparseArray
import com.darcy.message.lib_common.sparsearray.TestSparseBooleanArray
import com.darcy.message.lib_common.sparsearray.TestSparseIntArray
import com.darcy.message.lib_common.sparsearray.TestSparseLongArray
import com.darcy.message.lib_login.ui.login.LoginActivity
import com.darcy.message.lib_repackage.signature.SignatureHelper
import com.darcy.message.lib_repackage.signature.hook.PackageManagerHooker
import com.darcy.message.lib_startup.hook.InstrumentationHooker
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.lib_ui.base.mvp.test.TestMVPActivity
import com.darcy.message.lib_ui.exts.startPage
import com.darcy.message.lib_ui.mvi.ui.MVIActivity
import com.darcy.message.lib_ui.mvvm.sticky.StickyLiveDataActivity
import com.darcy.message.lib_ui.mvvm.ui.TestFragmentActivity
import com.darcy.message.lib_ui.paging.ui.TestPagingActivity
import com.darcy.message.sunflower.databinding.AppActivityMainBinding
import com.darcy.message.sunflower.lib_security.TestSecurityActivity
import com.darcy.message.sunflower.test.DataStoreActivity
import com.darcy.message.sunflower.test.RoomTestActivity
import com.darcy.message.sunflower.test.TestHttpActivity
import com.darcy.message.sunflower.test.TestJniActivity
import com.darcy.message.sunflower.test.TestWebSocketActivity
import com.darcy.message.sunflower.ui.detail.DetailActivity
import com.darcy.message.sunflower.ui.list.ListActivity
import com.darcy.message.sunflower.ui.notification.NotificationActivity


class MainActivity : BaseActivity<AppActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // follow system dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    override fun initView() {
        binding.innerFlutterPage.setOnClickListener {
//            FlutterActivityHelper.startFlutterActivity(this)
            FlutterActivityHelper.startFlutterActivity(
                this,
                FlutterPreloadHelper.FLUTTER_ENGINE_ID_1,
                "/")
        }
        binding.callFlutterApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).also {
                it.addCategory(Intent.CATEGORY_DEFAULT)
                it.type = "text/plain"
                it.putExtra(Intent.EXTRA_TEXT, "Tom and Jerry")
            }
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            println("FlutterActivity发送:$text")
            startActivity(intent)
        }
        binding.stickyLiveData.setOnClickListener {
            startPage(StickyLiveDataActivity::class.java)
        }
        binding.btnRoom.setOnClickListener {
            startPage(RoomTestActivity::class.java)
        }
        binding.btnRoomFlow.setOnClickListener {
            startPage(DetailActivity::class.java)
        }
        binding.btnPaging.setOnClickListener {
            startPage(TestPagingActivity::class.java)
        }
        binding.btnRoomPaging.setOnClickListener {
            startPage(ListActivity::class.java)
        }
        binding.btnMVI.setOnClickListener {
            startPage(MVIActivity::class.java)
        }
        binding.btnDataStore.setOnClickListener {
            startPage(DataStoreActivity::class.java)
        }
        binding.btnFragment.setOnClickListener {
            startPage(TestFragmentActivity::class.java)
        }
        binding.btnHttp.setOnClickListener {
            startPage(TestHttpActivity::class.java)
        }
        binding.btnWebSocket.setOnClickListener {
            startPage(TestWebSocketActivity::class.java)
        }
        binding.btnMvp.setOnClickListener {
            startPage(TestMVPActivity::class.java)
        }
        binding.btnSparseArray.setOnClickListener {
            TestSparseArray.test()
            TestSparseBooleanArray.test()
            TestSparseIntArray.test()
            TestSparseLongArray.test()
            TestArrayMap.test()
            TestArraySet.test()
        }
        binding.btnNotification.setOnClickListener {
            startPage(NotificationActivity::class.java)
        }
        binding.btnJni.setOnClickListener {
            startPage(TestJniActivity::class.java)
        }
        binding.btnSecurity.setOnClickListener {
            startPage(TestSecurityActivity::class.java)
        }
        binding.btnAppStatus.setOnClickListener {
            startPage(TestAppStatusActivity::class.java)
//            startActivity(Intent(this, TestAppStatusActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
//            })
        }
        binding.btnCamera.setOnClickListener {
            startPage(TestCameraActivity::class.java)
        }
        binding.btnCameraBackground.setOnClickListener {
            startPage(TestCameraBackgroundActivity::class.java)

            // 获取字体
            val typeface = ResourcesCompat.getFont(context, R.font.app_font_family)
            // 设置字体
            (it as Button).typeface = typeface
        }
        binding.btnCameraReceiver.setOnClickListener {
            startPage(TestCameraReceiverActivity::class.java)
            startService(Intent(AppHelper.getAppContext(), MessageService::class.java))
        }
        binding.btnSignature.setOnClickListener {
            SignatureHelper.getAppSignature(context, context.packageName)
            SignatureHelper.getAppSignature(context, "com.example.newhelloworld")
            SignatureHelper.getAppSignature(context, "com.microsoft.emmx")
        }
        binding.btnSignatureHook.setOnClickListener {
            PackageManagerHooker.hookPackageManager(context)
        }
        binding.btnInstrumentationHook.setOnClickListener {
            InstrumentationHooker.hookInstrumentation(context)
        }
        binding.btnInstrumentationHookReset.setOnClickListener {
            InstrumentationHooker.hookInstrumentation(context, reset = true)
        }
        binding.btnLogin.setOnClickListener {
            startPage(LoginActivity::class.java)
        }
    }


    override fun initListener() {

    }

    override fun initData() {

    }
}