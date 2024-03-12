package com.darcy.message.sunflower.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.darcy.message.sunflower.databinding.DetailItemBinding
import com.darcy.message.sunflower.ui.detail.bean.DetailBean

class DetailAdapter : PagingDataAdapter<DetailBean, DetailViewHolder>(Item_DIFF_CALLBACK) {
    companion object {
        private val Item_DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailBean>() {
            override fun areItemsTheSame(oldItem: DetailBean, newItem: DetailBean): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DetailBean, newItem: DetailBean): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean != null) {
            holder.bind(bean)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(
            DetailItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }
}