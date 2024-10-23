package com.darcy.message.sunflower.lib_security

import com.darcy.message.sunflower.lib_security.encryptor.impl.AndroidEncryptorImpl
import org.junit.Test

class AndroidEncryptorImplTest {
    @Test
    fun test_encrypt() {
        val encryptor = AndroidEncryptorImpl()
        val encrypt = encryptor.encrypt("hello world")
        println(encrypt)
    }
}