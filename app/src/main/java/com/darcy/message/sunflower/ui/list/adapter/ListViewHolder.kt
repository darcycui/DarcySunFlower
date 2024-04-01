package com.darcy.message.sunflower.ui.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.darcy.message.sunflower.databinding.DetailItemBinding
import com.darcy.message.sunflower.ui.list.bean.DetailBean

class ListViewHolder(
    private val binding: DetailItemBinding
) : RecyclerView.ViewHolder(binding.root){
    fun bind(detailBean: DetailBean) {
        binding.apply {
            binding.tvName.text = detailBean.name
            binding.tvPrice.text = detailBean.price.toString()
        }
    }
}
