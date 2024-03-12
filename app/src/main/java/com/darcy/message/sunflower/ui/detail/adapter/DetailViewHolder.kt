package com.darcy.message.sunflower.ui.detail.adapter

import androidx.recyclerview.widget.RecyclerView
import com.darcy.message.sunflower.databinding.DetailItemBinding
import com.darcy.message.sunflower.ui.detail.bean.DetailBean

class DetailViewHolder(
    private val binding: DetailItemBinding
) : RecyclerView.ViewHolder(binding.root){
    fun bind(detailBean: DetailBean) {
        binding.apply {
            binding.tvName.text = detailBean.name
            binding.tvPrice.text = detailBean.price.toString()
        }
    }
}
