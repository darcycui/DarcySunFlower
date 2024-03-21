package com.darcy.message.sunflower.ui.detail.di

import com.darcy.message.lib_common.app.AppHelper
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.db.impl.ItemDatabaseHelper
import com.darcy.message.lib_db.db.impl.ItemRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module for database.
 * InstallIn [SingletonComponent]
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    /**
     * Provides the itemDatabase.
     * Singleton.
     */
    @Provides
    @Singleton
    fun provideItemDatabase(): ItemRoomDatabase =
        ItemDatabaseHelper.getDatabase(AppHelper.getAppContext())

    /**
     * Provides the itemDao.
     */
    @Provides
    fun provideItemDao(database: ItemRoomDatabase): ItemDao = database.itemDao()
}