package com.darcy.message.lib_db.db.sqlcipher

import com.darcy.message.lib_common.exts.logD
import net.zetetic.database.sqlcipher.SQLiteConnection
import net.zetetic.database.sqlcipher.SQLiteDatabaseHook

class CustomCipherSQLiteHook : SQLiteDatabaseHook {
    companion object {
        const val TAG = "CustomCipherSQLiteHook"
    }

    /**
     * 在设置密钥前执行
     */
    override fun preKey(connection: SQLiteConnection) {
        logD("$TAG preKey")
        connection.execute("PRAGMA cipher_default_kdf_iter = 1;", null, null)
        connection.execute("PRAGMA cipher_default_page_size = 8192;", null, null)
    }

    /**
     * 在设置密钥后执行
     */
    override fun postKey(connection: SQLiteConnection) {
        logD("$TAG postKey")
        // 执行sql语句 设置PRAGMA参数
        // sqlcipher3(默认 KDF:SHA1 HMAC:SHA1) 或 sqlcipher4(默认 KDF:SHA512 HMAC:SHA512)
        connection.execute("PRAGMA cipher_compatibility = 4;", null, null)
        // 设置KDF:SHA256
        connection.execute("PRAGMA cipher_kdf_algorithm = PBKDF2_HMAC_SHA256;", null, null)
        // 设置HMAC:SHA256
        connection.execute("PRAGMA cipher_hmac_algorithm = HMAC_SHA256;", null, null)
        // kdf 迭代次数
        connection.execute("PRAGMA kdf_iter = '1';", null, null)
        // page 页大小
        connection.execute("PRAGMA cipher_page_size = 8192;", null, null);
    }
}