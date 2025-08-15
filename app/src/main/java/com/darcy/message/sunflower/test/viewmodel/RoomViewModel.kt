package com.darcy.message.sunflower.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darcy.message.lib_db.tables.Repo
import com.darcy.message.sunflower.test.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    suspend fun getItem(itemId: Int): Repo? {
        return repository.getItem(itemId)
    }

    suspend fun getItemLiveData(itemId: Int): LiveData<Repo?> {
        return repository.getItemLiveData(itemId)
    }

    suspend fun getItemFlow(itemId: Int): Flow<Repo?> {
        return repository.getItemFlow(itemId)
    }

    suspend fun getItems(): List<Repo>? {
        return repository.getItems()
    }

    suspend fun getItemsLiveData(): LiveData<List<Repo>?> {
        return repository.getItemsLiveData()
    }

    suspend fun getItemsFlow(): Flow<List<Repo>?> {
        return repository.getItemsFlow()
    }

    fun insertItem(repo: Repo) {
        viewModelScope.launch() {
            repository.insertItem(repo)
        }
    }

    fun deleteItem(repo: Repo) {
        viewModelScope.launch() {
            repository.deleteItem(repo)
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