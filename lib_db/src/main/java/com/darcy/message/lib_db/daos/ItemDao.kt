package com.darcy.message.lib_db.daos

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.darcy.message.lib_db.tables.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * from item WHERE id = :id")
    suspend fun getItem(id: Int): Item?

    @Query("SELECT * from item ORDER BY name ASC")
    suspend fun getItems(): List<Item>?

    /**
     * return LiveData from room directly.
     */
    @Query("SELECT * from item WHERE id = :id")
    fun getItemDetailLiveData(id: Int): LiveData<Item?>

    @Query("SELECT * from item ORDER BY name ASC")
    fun getItemsLiveData(): LiveData<List<Item>?>

    /**
     * return Flow from room directly.
     */
    @Query("SELECT * from item WHERE id = :id")
    fun getItemFlow(id: Int): Flow<Item?>

    @Query("SELECT * from item ORDER BY name ASC")
    fun getItemsFlow(): Flow<List<Item>?>

    @Query("SELECT * from item ORDER BY id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    suspend fun getItemsByPage(page: Int, pageSize: Int): List<Item>?

    /**
     * return PagingSource from room directly.
     */
    @Query("SELECT * from item ORDER BY id DESC")
    fun getItemsPagingSource(): PagingSource<Int, Item>
}