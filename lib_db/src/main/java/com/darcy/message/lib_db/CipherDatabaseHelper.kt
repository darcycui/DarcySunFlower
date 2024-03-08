package com.darcy.message.lib_db

import android.content.Context

object CipherDatabaseHelper {
    fun init(context: Context) {
        System.loadLibrary("sqlcipher")
    }

}