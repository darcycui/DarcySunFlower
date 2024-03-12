package com.darcy.message.lib_db.db.impl

import android.content.Context

object CipherDatabaseHelper {
    fun init(context: Context) {
        System.loadLibrary("sqlcipher")
    }

}