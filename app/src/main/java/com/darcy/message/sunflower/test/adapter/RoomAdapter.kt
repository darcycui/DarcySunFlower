package com.darcy.message.sunflower.test.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darcy.message.sunflower.databinding.AppDetailItemBinding
import com.darcy.message.sunflower.ui.list.adapter.viewholder.ListViewHolder
import com.darcy.message.sunflower.ui.list.bean.UiModel
import javax.inject.Inject

class RoomAdapter @Inject constructor() : RecyclerView.Adapter<ListViewHolder>() {
    private val data: MutableList<UiModel> = mutableListOf()

    fun setData(list: List<UiModel>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val bean = getItem(position)
        holder.bind(bean)
    }

    private fun getItem(position: Int): UiModel {
        return data[position]
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

    override fun getItemCount(): Int {
        return data.size
    }
}