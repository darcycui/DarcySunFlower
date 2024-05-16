package com.darcy.message.sunflower.ui.list.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.darcy.message.sunflower.databinding.AppDetailItemBinding
import com.darcy.message.sunflower.ui.list.bean.ListBean

class ListViewHolder(
    private val binding: AppDetailItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(detailBean: ListBean) {
        binding.run {
            tvName.text = "${detailBean.id} ${detailBean.name}"
            // keep only 2 decimal places
            tvPrice.text = String.format("%.2f", detailBean.time)
        }
    }
}
