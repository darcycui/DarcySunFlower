package com.darcy.message.lib_ui.paging.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.darcy.message.lib_im.R
import com.darcy.message.lib_im.databinding.LibUiItemLoadStateHeaderBinding

class LoadStateHeaderViewHolder(
    private val binding: LibUiItemLoadStateHeaderBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        // init item by loadState
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.loading.isVisible = loadState is LoadState.Loading
        binding.retry.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateHeaderViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.lib_ui_item_load_state_footer, parent, false)
            val binding = LibUiItemLoadStateHeaderBinding.bind(view)
            return LoadStateHeaderViewHolder(binding, retry)
        }
    }
}