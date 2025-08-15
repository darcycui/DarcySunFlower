package com.darcy.message.sunflower.ui.list.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.daos.RepoDao
import com.darcy.message.lib_db.db.impl.RepoRoomDatabase
import com.darcy.message.sunflower.ui.list.api.FakeApi
import com.darcy.message.sunflower.ui.list.api.GithubService
import com.darcy.message.sunflower.ui.list.repository.repo.RepoPagingSource
import com.darcy.message.sunflower.ui.list.repository.repo.RepoRepository
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val database: RepoRoomDatabase,
    private val itemDao: ItemDao,
    private val repoDao: RepoDao,
) {
    fun getItemsPagingFromRoom() = Pager(
        config = buildPagingConfig(),
        // create pagingSource by ListDataSource
        pagingSourceFactory = { ListDataSource(itemDao) }
    ).flow

    fun getItemsPagingFromHttp(query: String = "Android") = Pager(
        config = buildPagingConfig(),
        // create pagingSource by RepoPagingSource
        pagingSourceFactory = { RepoPagingSource(query, RepoRepository(GithubService.api())) }
    ).flow

    fun getItemsPagingFromRoomDao(query: String = "Android") = Pager(
        config = buildPagingConfig(),
        // create pagingSource by itemDao
//        pagingSourceFactory = { itemDao.getItemsPagingSourceASC() }
        pagingSourceFactory = { repoDao.reposByName("%$query%") }
    ).flow

    @OptIn(ExperimentalPagingApi::class)
    fun getPagingFromRoomAndHttp(query: String = "Android") = Pager(
        config = buildPagingConfig(),
        // create pagingSource by repoDao
        pagingSourceFactory = { repoDao.reposByName("%$query%") },
        // create remoteMediator here
        remoteMediator = ListRemoteMediator(query, FakeApi(), GithubService.api(), database)
    ).flow

    private fun buildPagingConfig(): PagingConfig = PagingConfig(
        pageSize = ITEMS_PER_PAGE,
        initialLoadSize = 1 * ITEMS_PER_PAGE,
        enablePlaceholders = false
    )
}