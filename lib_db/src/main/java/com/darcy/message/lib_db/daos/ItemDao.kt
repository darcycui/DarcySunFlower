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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Item>): List<Long>

    @Update
    suspend fun update(item: Item): Int

    @Delete
    suspend fun delete(item: Item): Int

    @Query("DELETE FROM item")
    suspend fun clearItems(): Int

    @Query("SELECT * from item WHERE id = :id")
    suspend fun getItem(id: Int): Item?

    @Query("SELECT * from item ORDER BY id ASC")
    suspend fun getItems(): List<Item>?

    /**
     * return LiveData from room directly.
     */
    @Query("SELECT * from item WHERE id = :id")
    fun getItemLiveData(id: Int): LiveData<Item?>

    @Query("SELECT * from item ORDER BY id ASC")
    fun getItemsLiveData(): LiveData<List<Item>?>

    /**
     * return Flow from room directly.
     */
    @Query("SELECT * from item WHERE id = :id")
    fun getItemFlow(id: Int): Flow<Item?>

    @Query("SELECT * from item ORDER BY id ASC")
    fun getItemsFlow(): Flow<List<Item>?>

    @Query("SELECT * from item ORDER BY id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    suspend fun getItemsByPage(page: Int, pageSize: Int): List<Item>?

    /**
     * return PagingSource from room directly.
     */
    @Query("SELECT * from item ORDER BY id DESC")
    fun getItemsPagingSourceDESC(): PagingSource<Int, Item>

    /**
     * return PagingSource from room directly.
     */
    @Query("SELECT * from item ORDER BY id ASC")
    fun getItemsPagingSourceASC(): PagingSource<Int, Item>
}