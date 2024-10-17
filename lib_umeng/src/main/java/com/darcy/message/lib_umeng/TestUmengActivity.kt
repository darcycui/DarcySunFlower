package com.darcy.message.lib_umeng

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.message.lib_umeng.databinding.ActivityTestUmentBinding
import com.umeng.analytics.MobclickAgent

class TestUmengActivity : AppCompatActivity() {
    private val TAG by lazy { this::class.java.simpleName }
    private val USER_ID by lazy { "user_001" }

    private val binding: ActivityTestUmentBinding by lazy {
        ActivityTestUmentBinding.inflate(layoutInflater)
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
        ReportManager.show("show:$TAG")
        MobclickAgent.onResume(this);
    }

    override fun onPause() {
        super.onPause()
        ReportManager.hide("hide:$TAG")
        MobclickAgent.onPause(this)
    }

    private fun initView() {
        binding.run {
            btnLogin.setOnClickListener {
                ReportManager.click("login")
                ReportManager.signIn(USER_ID)
            }
            btnLogout.setOnClickListener {
                ReportManager.click("logout")
                ReportManager.signOut(USER_ID)
            }
            btnClickButton.setOnClickListener {
                ReportManager.click("ClickButton")
            }
            btnShow.setOnClickListener {
                ReportManager.show("show:$TAG")
            }
            btnHide.setOnClickListener {
                ReportManager.hide("hide:$TAG")
            }
            btnFinish.setOnClickListener {
                ReportManager.click("finish")
            }
        }
    }
}