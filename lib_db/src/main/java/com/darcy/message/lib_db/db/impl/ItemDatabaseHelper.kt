package com.darcy.message.lib_db.db.impl

import android.content.Context
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_db.db.IDatabaseHelper
import com.darcy.message.lib_db.tables.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

object ItemDatabaseHelper : IDatabaseHelper {
    override fun init(context: Context) {
        logD(message = "ItemDatabaseHelper init")
    }

    override fun getDatabase(context: Context): ItemRoomDatabase {
        return ItemRoomDatabase.getDatabase(context)
    }

    fun testDB(context: Context) {
        GlobalScope.launch {
            getDatabase(context).itemDao().insert(Item(1, "Tom", 3.14, 100))
            getDatabase(context).itemDao().getItem(1).onEach {
                logD(message = it.toString())
            }.collect()
        }
    }
}