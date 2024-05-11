package com.darcy.message.sunflower.ui.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.darcy.message.sunflower.databinding.AppDetailItemBinding
import com.darcy.message.sunflower.ui.list.bean.ListBean

class ListViewHolder(
    private val binding: AppDetailItemBinding
) : RecyclerView.ViewHolder(binding.root){
    fun bind(detailBean: ListBean) {
        binding.apply {
            binding.tvName.text = detailBean.name
            // keep only 2 decimal places
            binding.tvPrice.text = String.format("%.2f", detailBean.time)
        }
    }
}
