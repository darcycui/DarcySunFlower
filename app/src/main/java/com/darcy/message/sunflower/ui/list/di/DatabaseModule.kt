package com.darcy.message.sunflower.ui.list.di

import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.daos.RemoteKeysDao
import com.darcy.message.lib_db.daos.RepoDao
import com.darcy.message.lib_db.db.DatabaseManager
import com.darcy.message.lib_db.db.impl.RepoRoomDatabase
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
    fun provideItemDatabase(): RepoRoomDatabase =
        DatabaseManager.getDatabase() as RepoRoomDatabase

    /**
     * Provides the itemDao.
     */
    @Provides
    fun provideItemDao(database: RepoRoomDatabase): ItemDao = database.itemDao()

    /**
     * Provides the repoDao.
     */
    @Provides
    fun provideRepoDao(database: RepoRoomDatabase): RepoDao = database.reposDao()

    /**
     * Provides the remoteKeysDao.
     */
    @Provides
    fun provideRemoteKeysDao(database: RepoRoomDatabase): RemoteKeysDao = database.remoteKeysDao()
}