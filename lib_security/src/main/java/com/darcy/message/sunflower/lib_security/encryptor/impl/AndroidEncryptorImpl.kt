package com.darcy.message.sunflower.lib_security.encryptor.impl

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.sunflower.lib_security.encryptor.IEncryptor
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


class AndroidEncryptorImpl : IEncryptor {
    companion object {

        private const val AES_KEY_SIZE: Int = 256
        // IV长度
        private const val IV_LENGTH: Int = 12
        // GCM模式下的Tag长度
        private const val GCM_TAG_LENGTH: Int = 128
        // GCM模式附加认证数据
        private const val MAC_DATA: String = "Some additional data"

        // 密钥库类型
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"

        // 密钥库别名
        private const val AES_KEY_ALIAS: String = "myGcmKey"

        // 加密算法标准算法名称
        private const val AES_TRANSFORMATION = "AES/GCM/NoPadding"

        private const val EC_KEYSTORE_ALIAS = "EC_keystore_alias"

        private const val RSA_KEYSTORE_ALIAS = "RSA_keystore_alias"
        private const val RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding"
    }

    private var aesKey: SecretKey? = null
    private var signatureCache: ByteArray = byteArrayOf()

    init {
        // 生成非对称签名密钥到 KeyStore
        generateAndSaveSignKeyToKeyStore()
        // 生成非对称加密密钥到 KeyStore
        generateAndSaveEncKeyToKeyStore()

        // 生成并保存密钥到 KeyStore
        generateAndSaveAESKeyToKeyStore(AES_KEY_ALIAS)

        // 从 KeyStore 获取密钥
        aesKey = getAESKeyFromKeyStore(AES_KEY_ALIAS)
        if (aesKey != null) {
            // 使用密钥进行加密或解密操作
            logD("get key from keystore SUCCESS+++")
        } else {
            logD("get key from keystore FAILED---")
            // 处理密钥不存在的情况
            aesKey = generateAndSaveAESKeyToKeyStore(AES_KEY_ALIAS)

        }
        // 不能获取key(hide api)
//        val realKey = key as? AndroidKeyStoreSecretKey
        logV("AndroidEncryptorImpl key: ${aesKey?.toString()}")
    }

    private fun generateAndSaveEncKeyToKeyStore(): KeyPair {
        // 创建密钥生成器
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA,
            KEYSTORE_PROVIDER
        )
        // 配置密钥生成器参数
        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
            RSA_KEYSTORE_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).run {
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
            build()
        }
        // 生成密钥对
        kpg.initialize(parameterSpec)
        val kp = kpg.generateKeyPair()
        return kp
    }

    private fun generateAndSaveSignKeyToKeyStore(): KeyPair {
        // 创建密钥生成器
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            KEYSTORE_PROVIDER
        )
        // 配置密钥生成器参数
        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
            EC_KEYSTORE_ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        ).run {
//            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
            build()
        }
        // 生成密钥对
        kpg.initialize(parameterSpec)
        val kp = kpg.generateKeyPair()
        return kp
    }

    private fun getPrivateKey(keyName: String): PrivateKey {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
            load(null)
        }
        // 判断密钥是否存在
        if (!keyStore.containsAlias(keyName)) {
            return generateAndSaveSignKeyToKeyStore().private
        }
        val entry = keyStore.getEntry(keyName, null)
        if (entry !is KeyStore.PrivateKeyEntry) {
            return generateAndSaveSignKeyToKeyStore().private
        }
        return entry.privateKey
    }

    private fun getPublicKey(keyName: String): PublicKey {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
            load(null)
        }
        // 判断密钥是否存在
        if (!keyStore.containsAlias(keyName)) {
            return generateAndSaveSignKeyToKeyStore().public
        }
        val entry = keyStore.getEntry(keyName, null)
        if (entry !is KeyStore.PrivateKeyEntry) {
            return generateAndSaveSignKeyToKeyStore().public
        }
        return entry.certificate.publicKey
    }

    /**
     * 生成AES密钥并保存密钥到 KeyStore
     */
    private fun generateAndSaveAESKeyToKeyStore(keyName: String): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)

        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setUserAuthenticationRequired(false) // 不需要用户验证
            .setRandomizedEncryptionRequired(false) // 允许传入IV
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(AES_KEY_SIZE)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    /**
     * 从 KeyStore 获取AES密钥
     */
    private fun getAESKeyFromKeyStore(keyName: String): SecretKey? {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)
        return keyStore.getKey(keyName, null) as? SecretKey
    }

    override fun encrypt(data: String): String {
        val plaintext: ByteArray = data.toByteArray()
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
//        val iv = cipher.iv
        // 随机生成IV
        val iv = ByteArray(IV_LENGTH)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(iv)
        val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmParameterSpec)
        // 附加认证数据(用于生成消息验证码MAC)
        cipher.updateAAD(MAC_DATA.toByteArray())
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        // 合并iv 和 ciphertext
        val messageArray = ByteArray(iv.size + ciphertext.size)
        System.arraycopy(iv, 0, messageArray, 0, iv.size)
        System.arraycopy(ciphertext, 0, messageArray, iv.size, ciphertext.size)
        // 消息摘要
        val md = MessageDigest.getInstance("SHA-256")
        val digest: ByteArray = md.digest(messageArray)
        val digestStr = Base64.encodeToString(digest, Base64.DEFAULT)
        logV("encrypt digestStr: $digestStr")
        // 合并messageArray 和 digest
        val resultArray = ByteArray(messageArray.size + digest.size)
        System.arraycopy(messageArray, 0, resultArray, 0, messageArray.size)
        System.arraycopy(digest, 0, resultArray, messageArray.size, digest.size)

        signatureCache = sign(resultArray)
        return Base64.encodeToString(resultArray, Base64.DEFAULT)
    }

    override fun encrypt(data: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    override fun decrypt(data: String): String {
        val ciphertext: ByteArray = Base64.decode(data, Base64.DEFAULT)
        verify(ciphertext, signatureCache)
        // 读取后256位为消息摘要
        val digest: ByteArray = ciphertext.copyOfRange(ciphertext.size - 32, ciphertext.size)
        val digestStr = Base64.encodeToString(digest, Base64.DEFAULT)
        logI("decrypt digestStr: $digestStr")
        // 读取密文和iv
        val ciphertextWithoutDigest: ByteArray = ciphertext.copyOfRange(0, ciphertext.size - 32)
        // 消息摘要
        val md = MessageDigest.getInstance("SHA-256")
        val digest2: ByteArray = md.digest(ciphertextWithoutDigest)
        val digestStr2 = Base64.encodeToString(digest2, Base64.DEFAULT)
        logD("decrypt digestStr2: $digestStr2")
        // 比较digest 和 digest2
        if (!digest.contentEquals(digest2)) {
            logD("decrypt digest not equals digest2")
            return "Error: Digest not equals digest2"
        }
        // 读取前IV_LENGTH位为iv
        val iv: ByteArray = ciphertext.copyOfRange(0, IV_LENGTH)
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, aesKey, gcmParameterSpec)
        // 附加认证数据(用于生成消息验证码MAC)
        cipher.updateAAD(MAC_DATA.toByteArray())
        // 读取密文
        val ciphertextWithoutDigestIv: ByteArray =
            ciphertext.copyOfRange(IV_LENGTH, ciphertext.size - 32)
        return String(cipher.doFinal(ciphertextWithoutDigestIv))
    }

    override fun decrypt(data: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    override fun sign(data: ByteArray): ByteArray {
        // 签名
        val privateKey: PrivateKey = getPrivateKey(EC_KEYSTORE_ALIAS)
        val s = Signature.getInstance("SHA256withECDSA")
            .apply {
                initSign(privateKey)
                update(data)
            }
        val signature: ByteArray = s.sign()
        val signStr = Base64.encodeToString(signature, Base64.DEFAULT)
        logV("encrypt signStr: $signStr")
        return signature
    }

    override fun verify(data: ByteArray, sign: ByteArray): Boolean {
        // 验签
        val signature: ByteArray = sign
        val publicKey: PublicKey = getPublicKey(EC_KEYSTORE_ALIAS)
        val s = Signature.getInstance("SHA256withECDSA")
            .apply {
                initVerify(publicKey)
                update(data)
            }
        val valid: Boolean = s.verify(signature)
        if (!valid) {
            logD("decrypt signature not valid")
            return false
        }
        logD("decrypt signature valid")
        return true
    }

    override fun encryptAsymmetric(data: ByteArray): ByteArray {
        val publicKey: PublicKey = getPublicKey(RSA_KEYSTORE_ALIAS)
        return Cipher.getInstance(RSA_TRANSFORMATION)
            .apply {
                init(Cipher.ENCRYPT_MODE, publicKey)
            }
            .doFinal(data)
    }

    override fun decryptAsymmetric(data: ByteArray): ByteArray {
        val privateKey: PrivateKey = getPrivateKey(RSA_KEYSTORE_ALIAS)
        return Cipher.getInstance(RSA_TRANSFORMATION)
            .apply {
                init(Cipher.DECRYPT_MODE, privateKey)
            }
            .doFinal(data)
    }
}