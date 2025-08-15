package com.darcy.message.sunflower.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.darcy.message.sunflower.databinding.AppDetailItemBinding
import com.darcy.message.sunflower.ui.list.adapter.viewholder.ListViewHolder
import com.darcy.message.sunflower.ui.list.bean.UiModel
import javax.inject.Inject

class ReposAdapter @Inject constructor() :
    PagingDataAdapter<UiModel, ListViewHolder>(Item_DIFF_CALLBACK) {
    companion object {
        private val Item_DIFF_CALLBACK = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean != null) {
            holder.bind(bean)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            AppDetailItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }
}