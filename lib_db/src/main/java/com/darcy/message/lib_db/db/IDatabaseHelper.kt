package com.darcy.message.lib_db.db

import android.content.Context

interface IDatabaseHelper {

    fun init(context: Context)

    fun getDatabase(context: Context): IDatabase
}