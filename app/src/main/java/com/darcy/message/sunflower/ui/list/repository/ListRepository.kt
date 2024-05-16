package com.darcy.message.sunflower.ui.list.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.db.impl.ItemRoomDatabase
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.sunflower.ui.list.api.NewsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val database: ItemRoomDatabase,
    private val itemDao: ItemDao
) {
    fun getItemsPaging() = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            initialLoadSize = 3 * ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        // create pagingSource
        pagingSourceFactory = { ListDateSource(itemDao) }
    ).flow

    fun getItemsPagingFromRoom() = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            initialLoadSize = 3 * ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        // create pagingSource by itemDao
        pagingSourceFactory = { itemDao.getItemsPagingSourceDESC() }
    ).flow

    @OptIn(ExperimentalPagingApi::class)
    fun getPagingFromRoomAndHttp() = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            initialLoadSize = 3 * ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        // create pagingSource by itemDao
        pagingSourceFactory = { itemDao.getItemsPagingSourceASC() },
        remoteMediator = ListRemoteMediator(NewsApi(), database)
    ).flow

    suspend fun loadDataLiveData(): LiveData<List<Item>?> {
        return withContext(Dispatchers.IO) {
            itemDao.getItemsLiveData()
        }
    }

    suspend fun loadDataFlow(): Flow<List<Item>?> {
        return withContext(Dispatchers.IO) {
            itemDao.getItemsFlow()
        }
    }

    suspend fun loadData(): List<Item>? {
        return withContext(Dispatchers.IO) {
            itemDao.getItems()
        }
    }

    suspend fun loadData(page: Int, pageSize: Int): List<Item> {
        delay(2_000)
        return withContext(Dispatchers.IO) {
            itemDao.getItemsByPage(page, pageSize) ?: listOf()
        }
    }
}