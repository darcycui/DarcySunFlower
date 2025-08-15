package com.darcy.message.sunflower.ui.list.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.darcy.message.sunflower.databinding.AppDetailItemBinding
import com.darcy.message.sunflower.ui.list.bean.UiModel

class ListViewHolder(
    private val binding: AppDetailItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(detailBean: UiModel) {
        binding.run {
            tvName.text = detailBean.name
            tvPrice.text = detailBean.description
        }
    }
}
