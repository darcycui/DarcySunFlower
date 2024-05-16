package com.darcy.message.lib_db.db.impl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.daos.RemoteKeysDao
import com.darcy.message.lib_db.db.IDatabase
import com.darcy.message.lib_db.migrations.ItemMigration1To2
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.lib_db.tables.RemoteKeys
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.nio.charset.StandardCharsets

@Database(
    entities = [Item::class, RemoteKeys::class],
    version = 1,
    exportSchema = true
)
abstract class ItemRoomDatabase : RoomDatabase(), IDatabase {
    abstract fun itemDao(): ItemDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    override fun show() {
        logD(message = "This is ItemRoomDatabase.")
    }

    companion object {
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null

        fun getDatabase(context: Context): ItemRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                // use sqlcipher factory create db
//                CipherDatabaseHelper.init(context)
//                val factory = SupportOpenHelperFactory(
//                    "darcy123456".toByteArray(StandardCharsets.UTF_8)
//                )
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                ).fallbackToDestructiveMigration() // fallback strategy
                    .addMigrations(ItemMigration1To2()) // migrations
//                    .openHelperFactory(factory) // use custom factory
//                    .allowMainThreadQueries() // mai thread use
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}