package com.darcy.message.sunflower.ui.detail.repository

import androidx.lifecycle.LiveData
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.db.impl.RepoRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.darcy.message.lib_db.tables.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val repoRoomDatabase: RepoRoomDatabase
) {
    suspend fun getItemDetailLiveData(itemId: Int): LiveData<Repo?> {
        return withContext(Dispatchers.IO) {
            itemDao.getItemLiveData(itemId)
        }
    }
    suspend fun getItemDetailFlow(itemId: Int): Flow<Repo?> {
        return withContext(Dispatchers.IO) {
            itemDao.getItemFlow(itemId)
        }
    }

    suspend fun updateItem(itemId: Int, itemName: String) {
        withContext(Dispatchers.IO) {
            itemDao.getItem(itemId)?.let {
                it.name = itemName
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
            repoRoomDatabase.runInTransaction {
                logD(message = "runInTransaction-${Thread.currentThread().id}")
                val scope = CoroutineScope(Dispatchers.Default)
                // darcyRefactor do not launch new coroutine in transaction ???
                scope.launch {
                    logD(message = "runInTransaction-scope-${Thread.currentThread().id}")
                    val item = itemDao.getItem(1)
                    item?.let {
                        it.name = "Tom In Transaction ${System.currentTimeMillis()}"
                        itemDao.update(it)
                    }
                }
            }
        }
    }
}