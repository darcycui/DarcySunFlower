package com.darcy.message.lib_db.daos

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.darcy.message.lib_db.tables.Repo
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repo: Repo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Repo>): List<Long>

    @Update
    suspend fun update(repo: Repo): Int

    @Delete
    suspend fun delete(repo: Repo): Int

    @Query("DELETE From repos")
    suspend fun clearItems(): Int

    @Query("SELECT * From repos WHERE id = :id")
    suspend fun getItem(id: Int): Repo?

    @Query("SELECT * From repos ORDER BY id ASC")
    suspend fun getItems(): List<Repo>?

    /**
     * return LiveData From room directly.
     */
    @Query("SELECT * From repos WHERE id = :id")
    fun getItemLiveData(id: Int): LiveData<Repo?>

    @Query("SELECT * From repos ORDER BY id ASC")
    fun getItemsLiveData(): LiveData<List<Repo>?>

    /**
     * return Flow From room directly.
     */
    @Query("SELECT * From repos WHERE id = :id")
    fun getItemFlow(id: Int): Flow<Repo?>

    @Query("SELECT * From repos ORDER BY id ASC")
    fun getItemsFlow(): Flow<List<Repo>?>

    @Query("SELECT * From repos ORDER BY id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    suspend fun getItemsByPage(page: Int, pageSize: Int): List<Repo>?

    /**
     * return PagingSource From room directly.
     */
    @Query("SELECT * From repos ORDER BY id DESC")
    fun getItemsPagingSourceDESC(): PagingSource<Int, Repo>

    /**
     * return PagingSource From room directly.
     */
    @Query("SELECT * From repos ORDER BY id ASC")
    fun getItemsPagingSourceASC(): PagingSource<Int, Repo>
}