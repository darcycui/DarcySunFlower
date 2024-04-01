package com.darcy.message.sunflower.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.darcy.message.sunflower.databinding.DetailItemBinding
import com.darcy.message.sunflower.ui.list.bean.DetailBean
import javax.inject.Inject

class ListAdapter @Inject constructor() : PagingDataAdapter<DetailBean, ListViewHolder>(Item_DIFF_CALLBACK) {
    companion object {
        private val Item_DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailBean>() {
            override fun areItemsTheSame(oldItem: DetailBean, newItem: DetailBean): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DetailBean, newItem: DetailBean): Boolean =
                oldItem == newItem
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
            DetailItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }
}