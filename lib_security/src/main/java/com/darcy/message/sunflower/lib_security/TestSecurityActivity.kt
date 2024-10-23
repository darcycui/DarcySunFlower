package com.darcy.message.sunflower.lib_security

import android.os.Bundle
import android.util.Base64
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_security.R
import com.darcy.message.lib_security.databinding.LibSecurityActivityTestSecurityBinding

class TestSecurityActivity : AppCompatActivity() {
    private val binding: LibSecurityActivityTestSecurityBinding by lazy {
        LibSecurityActivityTestSecurityBinding.inflate(layoutInflater)
    }
    private val originalText= "123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.lib_security_activity_test_security)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    private fun initView() {
        binding.run {
            btnEncrypt.setOnClickListener {
                tvOriginal.text = originalText
                val encrypt = SecurityManager.encrypt(originalText)
                tvEncrypt.text = encrypt
                tvDecrypt.text = getString(R.string.lib_security_default)
            }
            btnDecrypt.setOnClickListener {
                val decrypt = SecurityManager.decrypt(tvEncrypt.text.toString())
                tvDecrypt.text = decrypt
            }
            btnEncryptEC.setOnClickListener {
                logI("加密前: $originalText")
                val encrypt = SecurityManager.encryptAsymmetric(originalText.toByteArray())
                tvEncrypt.text = Base64.encodeToString(encrypt, Base64.DEFAULT).also {
                    logI("加密后: $it")
                }
                tvDecrypt.text = getString(R.string.lib_security_default)
            }
            btnDecryptEC.setOnClickListener {
                val decrypt = SecurityManager.decryptAsymmetric(
                    Base64.decode(
                        tvEncrypt.text.toString().also {
                            logI("解密前: $it")
                        },
                        Base64.DEFAULT
                    )
                )
                tvDecrypt.text = String(decrypt).also {
                    logI("解密后: $it")
                }
            }
        }
    }
}