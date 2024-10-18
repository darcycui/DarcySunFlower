package com.darcy.message.sunflower.test

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.sunflower.R
import com.darcy.message.sunflower.databinding.AppActivityTestJniBinding
import com.jni.rust.RustNative
import jni.TestJni

class TestJniActivity : BaseActivity<AppActivityTestJniBinding>() {
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
    }

    override fun initListener() {
        binding.btnTestJni.setOnClickListener {
            val str = TestJni.getString()
            logD("str: $str")
            binding.tvTestJni.text = "$str ${count++}"
            val strRustEnc = RustNative.getEncryptStringFromRust("test rust AES")
            binding.tvTestJniEncryptRust.text = "$strRustEnc ${count++}"
            val strRustDec = RustNative.getDecryptStringFromRust(strRustEnc)
            binding.tvTestJniDecryptRust.text = "$strRustDec ${count++}"
        }
    }

    override fun initData() {
    }
}