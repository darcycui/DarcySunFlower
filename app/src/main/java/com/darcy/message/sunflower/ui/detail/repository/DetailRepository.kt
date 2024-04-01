package com.darcy.message.sunflower.ui.detail.repository

import com.darcy.message.lib_db.daos.ItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.darcy.message.lib_db.tables.Item
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailRepository @Inject constructor(private val itemDao: ItemDao) {
    suspend fun getItemDetail(itemId: Int): Flow<Item?> {
        return withContext(Dispatchers.IO) {
            itemDao.getItemFlow(itemId)
        }
    }

    suspend fun updateItem(itemId: Int, itemName: String) {
        withContext(Dispatchers.IO) {
            itemDao.getItem(itemId)?.let {
                it.itemName = itemName
                itemDao.update(it)
            }
        }
    }
}