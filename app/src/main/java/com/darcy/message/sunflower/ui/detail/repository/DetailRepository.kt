package com.darcy.message.sunflower.ui.detail.repository

import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.tables.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DetailRepository(private val itemDao: ItemDao) {
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