package com.darcy.message.sunflower.test.repository

import androidx.lifecycle.LiveData
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.daos.RepoDao
import com.darcy.message.lib_db.db.impl.ItemRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.lib_db.tables.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val repoDao: RepoDao,
    private val itemRoomDatabase: ItemRoomDatabase
) {
    suspend fun getItem(itemId: Int): Item? {
        return withContext(Dispatchers.IO) {
            itemDao.getItem(itemId)
        }
    }

    suspend fun getItemLiveData(itemId: Int): LiveData<Item?> {
        return withContext(Dispatchers.IO) {
            itemDao.getItemLiveData(itemId)
        }
    }

    suspend fun getItemFlow(itemId: Int): Flow<Item?> {
        return withContext(Dispatchers.IO) {
            itemDao.getItemFlow(itemId)
        }
    }

    suspend fun getItems(): List<Item>? {
        return withContext(Dispatchers.IO) {
            itemDao.getItems()
        }
    }

    suspend fun getItemsLiveData(): LiveData<List<Item>?> {
        return withContext(Dispatchers.IO) {
            itemDao.getItemsLiveData()
        }
    }

    suspend fun getItemsFlow(): Flow<List<Item>?> {
        return withContext(Dispatchers.IO) {
            itemDao.getItemsFlow()
        }
    }

    suspend fun insertItem(item: Item): Long {
        return withContext(Dispatchers.IO) {
            itemDao.insert(item).also {
                logD(message = "insertItem-$it")
            }
        }
    }

    suspend fun deleteItem(item: Item): Int {
        return withContext(Dispatchers.IO) {
            itemDao.delete(item)
        }
    }

    suspend fun deleteItemAll(): Int {
        return withContext(Dispatchers.IO) {
            itemDao.clearItems().also {
                logD(message = "deleteItemAll-$it")
            }
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

    /**
     * use transaction to update db data.
     * // darcyRefactor how to use suspend function in transaction ???
     */
    suspend fun updateByDBTransaction() {
        withContext(Dispatchers.IO) {
            itemRoomDatabase.runInTransaction {
                logD(message = "runInTransaction-${Thread.currentThread().id}")
                val scope = CoroutineScope(Dispatchers.Default)
                // darcyRefactor do not launch new coroutine in transaction ???
                scope.launch {
                    logD(message = "runInTransaction-scope-${Thread.currentThread().id}")
                    val item = itemDao.getItem(1)
                    item?.let {
                        it.itemName = "Tom In Transaction ${System.currentTimeMillis()}"
                        itemDao.update(it)
                    }
                }
            }
        }
    }

    suspend fun getRepos(queryString: String): List<Repo>? {
        return withContext(Dispatchers.IO) {
            repoDao.getRepos(queryString)
        }
    }
}