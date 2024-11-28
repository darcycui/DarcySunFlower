package com.darcy.message.lib_repackage.signature

import android.content.Context
import android.content.pm.PackageManager
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_common.exts.print
import java.security.MessageDigest

object SignatureHelper {
    private val TAG = "SignatureHelper"

    /**
     * 获取apk签名,支持当前app和其他app
     */
    fun getAppSignature(context: Context, packageName: String): String {
        val sb: StringBuilder = StringBuilder()
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                packageName, PackageManager.GET_SIGNATURES
            )
            val signatureArr = packageInfo.signatures
            val messageDigest = MessageDigest.getInstance("SHA1")
            messageDigest.update(signatureArr[0].toByteArray())
            val digest = messageDigest.digest()
            for (b2 in digest) {
                sb.append(String.format("%02x", b2))
            }
        } catch (unused: Exception) {
            logE("$TAG error: $unused")
            unused.print()
        }
        logV("$TAG: $sb")
        return sb.toString()
    }
}