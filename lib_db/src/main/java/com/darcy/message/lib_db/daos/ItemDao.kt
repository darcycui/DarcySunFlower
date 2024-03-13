package com.darcy.message.lib_db.daos

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
    fun getItemFlow(id: Int): Flow<Item?>

    @Query("SELECT * from item ORDER BY name ASC")
    fun getItemsFlow(): Flow<List<Item>?>

    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Item?

    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems(): List<Item>?

    @Query("SELECT * from item ORDER BY id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    fun getItemsByPage(page: Int, pageSize: Int): List<Item>?
}