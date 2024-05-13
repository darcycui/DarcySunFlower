package com.darcy.message.sunflower.ui.list.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.darcy.message.sunflower.R
import com.darcy.message.sunflower.databinding.AppRecyclerviewHeaderFooterBinding

class LoadStateViewHolder(parent: ViewGroup, var retry: () -> Unit) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.app_recyclerview_header_footer, parent, false)
) {

    private var binding: AppRecyclerviewHeaderFooterBinding = AppRecyclerviewHeaderFooterBinding.bind(itemView)

    fun bindState(loadState: LoadState) {
        binding.btnRetry.setOnClickListener {
            retry()
        }
        binding.llLoading.isVisible = loadState is LoadState.Loading
        binding.btnRetry.isVisible = loadState is LoadState.Error

    }

}