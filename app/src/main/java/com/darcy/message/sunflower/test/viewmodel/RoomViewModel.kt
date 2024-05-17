package com.darcy.message.sunflower.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.lib_db.tables.Repo
import com.darcy.message.sunflower.test.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @HiltViewModel
 * use hilt inject ViewModel
 */
@HiltViewModel
class RoomViewModel @Inject constructor(private val repository: RoomRepository) :
    ViewModel() {
    suspend fun getItem(itemId: Int): Item? {
        return repository.getItem(itemId)
    }

    suspend fun getItemLiveData(itemId: Int): LiveData<Item?> {
        return repository.getItemLiveData(itemId)
    }

    suspend fun getItemFlow(itemId: Int): Flow<Item?> {
        return repository.getItemFlow(itemId)
    }

    suspend fun getItems(): List<Item>? {
        return repository.getItems()
    }

    suspend fun getItemsLiveData(): LiveData<List<Item>?> {
        return repository.getItemsLiveData()
    }

    suspend fun getItemsFlow(): Flow<List<Item>?> {
        return repository.getItemsFlow()
    }

    fun insertItem(item: Item) {
        viewModelScope.launch() {
            repository.insertItem(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch() {
            repository.deleteItem(item)
        }
    }

    fun deleteItemAll() {
        viewModelScope.launch() {
            repository.deleteItemAll()
        }
    }

    fun updateItem(itemId: Int, itemName: String) {
        viewModelScope.launch {
            repository.updateItem(itemId, itemName)
        }
    }

    suspend fun getRepos(): List<Repo>? {
        return repository.getRepos("Android")
    }
}