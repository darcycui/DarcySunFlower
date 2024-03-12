package com.darcy.message.sunflower.ui.detail.repository

import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.tables.Item
import kotlinx.coroutines.flow.Flow

class DetailRepository(private val itemDao: ItemDao) {
    suspend fun loadDataFlow(): Flow<List<Item>?> {
        return itemDao.getItemsFlow()
    }

    suspend fun loadData(currentPage: Int): List<Item>? {
        return itemDao.getItems()
    }
}