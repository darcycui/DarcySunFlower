package com.darcy.message.sunflower.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_common.exts.logW
import com.darcy.message.sunflower.databinding.AppDetailItemBinding
import com.darcy.message.sunflower.ui.list.adapter.viewholder.ListViewHolder
import com.darcy.message.sunflower.ui.list.bean.ListBean
import javax.inject.Inject

class ListAdapter @Inject constructor() :
    PagingDataAdapter<ListBean, ListViewHolder>(Item_DIFF_CALLBACK) {
    companion object {
        private val Item_DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListBean>() {
            override fun areItemsTheSame(oldItem: ListBean, newItem: ListBean): Boolean {
//                if (oldItem.id == newItem.id) {
//                    logV("areItemsTheSame", message = "oldItem=(${oldItem.id}) and newItem=(${newItem.id})--SAME")
//                } else {
//                    logE("areItemsTheSame", message = "oldItem=(${oldItem.id}) and newItem=(${newItem.id})--NOT SAME")
//                }
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListBean, newItem: ListBean): Boolean {
//                if (oldItem == newItem) {
//                    logI("areContentsTheSame", message = "oldItem(${oldItem.id}) and newItem(${newItem.id}):SAME")
//                } else {
//                    logW("areContentsTheSame", message = "oldItem(${oldItem.id}) and newItem(${newItem.id}):NOT SAME")
//                }
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