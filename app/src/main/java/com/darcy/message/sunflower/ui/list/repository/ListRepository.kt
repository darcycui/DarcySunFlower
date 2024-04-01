package com.darcy.message.sunflower.ui.list.repository

import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.tables.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListRepository @Inject constructor (private val itemDao: ItemDao) {
    suspend fun loadDataFlow(): Flow<List<Item>?> {
        return withContext(Dispatchers.IO) {
            itemDao.getItemsFlow()
        }
    }

    suspend fun loadData(page: Int, pageSize: Int): List<Item> {
        return withContext(Dispatchers.IO) {
            itemDao.getItemsByPage(page, pageSize) ?: listOf()
        }
    }
}