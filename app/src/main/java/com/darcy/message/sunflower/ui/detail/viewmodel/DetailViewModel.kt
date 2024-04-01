package com.darcy.message.sunflower.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.darcy.message.lib_db.tables.Item
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
    suspend fun getItemDetail(itemId: Int): Flow<Item?> {
        return detailRepository.getItemDetail(itemId)
    }

    suspend fun updateItem(itemId: Int, itemName: String) {
        detailRepository.updateItem(itemId, itemName)
    }
}