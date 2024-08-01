package com.darcy.message.sunflower.ui.list.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.daos.RepoDao
import com.darcy.message.lib_db.db.impl.ItemRoomDatabase
import com.darcy.message.sunflower.ui.list.api.GithubService
import com.darcy.message.sunflower.ui.list.api.TestApi
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val database: ItemRoomDatabase,
    private val itemDao: ItemDao,
    private val repoDao: RepoDao,
) {
    fun getItemsPaging() = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            initialLoadSize = 3 * ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        // create pagingSource
        pagingSourceFactory = { ListDataSource(itemDao) }
    ).flow

    fun getItemsPagingFromRoom() = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            initialLoadSize = 3 * ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        // create pagingSource by itemDao
        pagingSourceFactory = { itemDao.getItemsPagingSourceASC() }
    ).flow

    @OptIn(ExperimentalPagingApi::class)
    fun getPagingFromRoomAndHttp() = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            initialLoadSize = 3 * ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        // create pagingSource by repoDao
        pagingSourceFactory = { repoDao.reposByName() },
        // create remoteMediator here
        remoteMediator = ListRemoteMediator(TestApi(), GithubService.api(), database)
    ).flow
}