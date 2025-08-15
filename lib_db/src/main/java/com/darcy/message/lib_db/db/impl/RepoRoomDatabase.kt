package com.darcy.message.lib_db.db.impl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_db.db.IDatabase
import com.darcy.message.lib_db.db.sqlcipher.CustomCipherSQLiteHook
import com.darcy.message.lib_db.migrations.RepoMigration1To2
import com.darcy.message.lib_db.tables.RemoteKeys
import com.darcy.message.lib_db.tables.Repo
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.nio.charset.StandardCharsets

@Database(
    entities = [Repo::class, RemoteKeys::class],
    version = 1,
    exportSchema = true
)
abstract class RepoRoomDatabase : RoomDatabase(), IDatabase {

    override fun show() {
        logD(message = "This is ItemRoomDatabase.")
    }

    companion object {
        @Volatile
        private var INSTANCE: RepoRoomDatabase? = null

        fun getDatabase(context: Context): RepoRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                // use default sqlite database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RepoRoomDatabase::class.java,
                    "item_database"
                ).fallbackToDestructiveMigration() // fallback strategy
                    .addMigrations(RepoMigration1To2()) // migrations
//                    .allowMainThreadQueries() // mai thread use
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        fun getCipherDatabase(context: Context): RepoRoomDatabase {
            // load sqlcipher native library
            System.loadLibrary("sqlcipher")
            return INSTANCE ?: synchronized(this) {
                // use sqlcipher factory create db
                val factory = SupportOpenHelperFactory(
                    "darcy123456".toByteArray(StandardCharsets.UTF_8),
                    CustomCipherSQLiteHook(),
                    true
                )
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RepoRoomDatabase::class.java,
                    "item_cipher_database"
                ).fallbackToDestructiveMigration() // fallback strategy
                    .addMigrations(RepoMigration1To2()) // migrations
                    .openHelperFactory(factory) // use custom factory
//                    .allowMainThreadQueries() // mai thread use
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}