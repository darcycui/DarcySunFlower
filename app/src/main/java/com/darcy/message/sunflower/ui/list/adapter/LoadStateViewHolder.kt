package com.darcy.message.sunflower.ui.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.darcy.message.sunflower.R
import com.darcy.message.sunflower.databinding.RecyclerviewHeaderFooterBinding

class LoadStateViewHolder(parent: ViewGroup, var retry: () -> Unit) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.recyclerview_header_footer, parent, false)
) {

    private var itemLoadStateBindingUtil: RecyclerviewHeaderFooterBinding = RecyclerviewHeaderFooterBinding.bind(itemView)

    fun bindState(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            itemLoadStateBindingUtil.btnRetry.visibility = View.VISIBLE
            itemLoadStateBindingUtil.btnRetry.setOnClickListener {
                retry()
            }
        } else if (loadState is LoadState.Loading) {
            itemLoadStateBindingUtil.llLoading.visibility = View.VISIBLE
        }

    }

}