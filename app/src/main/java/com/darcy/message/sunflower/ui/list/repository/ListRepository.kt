package com.darcy.message.sunflower.ui.list.repository

import androidx.lifecycle.LiveData
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.tables.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListRepository @Inject constructor(private val itemDao: ItemDao) {
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