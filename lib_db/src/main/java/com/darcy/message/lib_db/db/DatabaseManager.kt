package com.darcy.message.lib_db.db

import android.content.Context
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_db.tables.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

object DatabaseManager {
    private var database: IDatabase? = null

    fun init(context: Context, database: IDatabase) {
        logD(message = "DatabaseManager init")
        this.database = database
    }

    fun getDatabase(): IDatabase {
        return database ?: throw IllegalStateException("Database not initialized")
    }

    fun testDB(context: Context) {
        GlobalScope.launch {
            repeat(1) { count ->
                logI(message = "insert item $count")
                getDatabase().itemDao().insert(Item(count, "TomJerry $count", count.toDouble(), 100))
                    .also { result ->
                        logI(message = "insert item $count result=$result")
                    }
            }
            getDatabase().itemDao().getItem(0).also {
                logD(message = "item0-->${it.toString()}")
            }
            getDatabase().itemDao().getItemsFlow().onEach {
                it?.onEach { item ->
                    logD(message = item.toString())
                } ?: run { logD(message = "Items Flow == null") }
            }.collect()

//            getDatabase().itemDao().getItems()?.onEach { item ->
//                logD(message = "$item")
//            } ?: run { logD(message = "Items == null") }
        }
    }
}