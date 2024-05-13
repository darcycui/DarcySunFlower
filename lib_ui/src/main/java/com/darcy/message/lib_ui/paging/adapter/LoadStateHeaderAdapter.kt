package com.darcy.message.lib_ui.paging.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.darcy.message.lib_ui.paging.adapter.viewholder.LoadStateHeaderViewHolder


/**
 * adapter for load state header
 */
class LoadStateHeaderAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadStateHeaderViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateHeaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateHeaderViewHolder {
        return LoadStateHeaderViewHolder.create(parent, retry)
    }


    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
//        return true
        return super.displayLoadStateAsItem(loadState)
    }
}

