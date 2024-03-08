package com.darcy.message.lib_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class ItemMigration1To2() : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("SELECT * from item ORDER BY name ASC")
    }
}