package com.darcy.message.sunflower.ui.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.darcy.message.lib_db.tables.Repo
import com.darcy.message.sunflower.ui.detail.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @HiltViewModel
 * use hilt inject ViewModel
 */
@HiltViewModel
class DetailViewModel @Inject constructor(private val detailRepository: DetailRepository) :
    ViewModel() {
    suspend fun getItemDetailFlow(itemId: Int): Flow<Repo?> {
        return detailRepository.getItemDetailFlow(itemId)
    }
    suspend fun getItemDetailLiveData(itemId: Int): LiveData<Repo?> {
        return detailRepository.getItemDetailLiveData(itemId)
    }

    suspend fun updateItem(itemId: Int, itemName: String) {
        detailRepository.updateItem(itemId, itemName)
    }

    suspend fun updateByDBTransaction() {
        detailRepository.updateByDBTransaction()
    }
}