package com.darcy.message.sunflower.lib_security

import com.darcy.message.sunflower.lib_security.encryptor.IEncryptor
import com.darcy.message.sunflower.lib_security.encryptor.impl.AndroidEncryptorImpl

object SecurityManager : IEncryptor {
    private val iEncryptor: IEncryptor = AndroidEncryptorImpl()
    override fun encrypt(data: String): String {
        return iEncryptor.encrypt(data)
    }

    override fun encrypt(data: ByteArray): ByteArray {
        return iEncryptor.encrypt(data)
    }

    override fun decrypt(data: String): String {
        return iEncryptor.decrypt(data)
    }

    override fun decrypt(data: ByteArray): ByteArray {
        return iEncryptor.decrypt(data)
    }

    override fun sign(data: ByteArray): ByteArray {
        return iEncryptor.sign(data)
    }

    override fun verify(data: ByteArray, sign: ByteArray): Boolean {
        return iEncryptor.verify(data, sign)
    }

    override fun encryptAsymmetric(data: ByteArray): ByteArray {
        return iEncryptor.encryptAsymmetric(data)
    }

    override fun decryptAsymmetric(data: ByteArray): ByteArray {
        return iEncryptor.decryptAsymmetric(data)
    }

}