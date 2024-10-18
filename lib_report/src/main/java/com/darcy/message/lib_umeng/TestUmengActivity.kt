package com.darcy.message.lib_umeng

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.message.lib_umeng.databinding.LibReportActivityTestUmentBinding
import com.umeng.analytics.MobclickAgent

class TestUmengActivity : AppCompatActivity() {
    private val TAG by lazy { this::class.java.simpleName }
    private val USER_ID by lazy { "user_001" }
    private val context by lazy { this }
    private val fragment: TopFragment by lazy { TopFragment.getInstance("test") }

    private val binding: LibReportActivityTestUmentBinding by lazy {
        LibReportActivityTestUmentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_test_ument)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    override fun onResume() {
        super.onResume()
//        ReportManager.show(context, "show:$TAG")
//        ReportManager.onResume(context, TAG)
    }

    override fun onPause() {
        super.onPause()
//        ReportManager.hide(context, "hide:$TAG")
//        ReportManager.onPause(context,TAG)
    }

    private fun initView() {
        binding.run {
            btnHome.setOnClickListener {
                // 根据包名和类名 创建隐式Intent 然后启动activity
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_LAUNCHER)
                    setClassName(packageName, "com.darcy.message.sunflower.MainActivity")
                }
                startActivity(intent)
                finish()
            }
            btnLogin.setOnClickListener {
                ReportManager.click(context, "login")
                ReportManager.signIn(USER_ID)
            }
            btnLogout.setOnClickListener {
                ReportManager.click(context, "logout")
                ReportManager.signOut(USER_ID)
            }
            btnClickButton.setOnClickListener {
                ReportManager.click(context, "ClickButton")
            }
            btnShow.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment).commitAllowingStateLoss()

            }
            btnHide.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .remove(fragment).commitAllowingStateLoss()
            }
            btnFinish.setOnClickListener {
                ReportManager.click(context, "finish")
            }
            btnCrash.setOnClickListener {
                MobclickAgent.reportError(context, "crash a")
                val a = 1 / 0
            }
        }
    }
}