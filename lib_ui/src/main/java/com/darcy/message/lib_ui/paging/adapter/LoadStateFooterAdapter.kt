package com.darcy.message.lib_ui.paging.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.darcy.message.lib_ui.paging.adapter.viewholder.LoadStateFooterViewHolder


/**
 * adapter for load state footer
 */
class LoadStateFooterAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadStateFooterViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateFooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateFooterViewHolder {
        return LoadStateFooterViewHolder.create(parent, retry)
    }


    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
//        return true
        return super.displayLoadStateAsItem(loadState)
    }
}

