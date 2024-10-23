package com.darcy.message.sunflower.lib_security.encryptor

interface IEncryptor {
    fun encrypt(data: String): String
    fun encrypt(data: ByteArray): ByteArray
    fun decrypt(data: String): String
    fun decrypt(data: ByteArray): ByteArray
    fun sign(data: ByteArray): ByteArray
    fun verify(data: ByteArray, sign: ByteArray): Boolean
    fun encryptAsymmetric(data: ByteArray): ByteArray
    fun decryptAsymmetric(data: ByteArray): ByteArray
}