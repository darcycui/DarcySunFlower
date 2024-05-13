package com.darcy.message.lib_ui.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_im.databinding.LibUiArticleViewholderBinding
import com.darcy.message.lib_im.databinding.LibUiItemMessageSeparatorBinding
import com.darcy.message.lib_ui.paging.adapter.viewholder.ArticleViewHolder
import com.darcy.message.lib_ui.paging.adapter.viewholder.SeparatorViewHolder
import com.darcy.message.lib_ui.paging.entity.IEntity.Article
import com.darcy.message.lib_ui.paging.entity.IEntity

/**
 * adapter for article/separator
 */
class MultipleTypeAdapter :
    PagingDataAdapter<IEntity, RecyclerView.ViewHolder>(ENTITY_DIFF_CALLBACK) {
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is Article) {
            IViewType.ArticleViewType().type
        } else {
            IViewType.SeparatorViewType().type
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        logD(message = "position=$position itemViewType=$viewType")
        when (viewType) {
            IViewType.ArticleViewType().type -> {
                (holder as ArticleViewHolder).bind(getItem(position) as Article)
            }

            IViewType.SeparatorViewType().type -> {
                (holder as SeparatorViewHolder).bind(getItem(position) as IEntity.SeparatorEntity)
            }

            else -> {
                throw IllegalStateException("Unknown view type")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IViewType.ArticleViewType().type -> {
                ArticleViewHolder(
                    LibUiArticleViewholderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                )
            }

            IViewType.SeparatorViewType().type -> {
                SeparatorViewHolder(
                    LibUiItemMessageSeparatorBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                )
            }

            else -> {
                throw IllegalStateException("Unknown view type")
            }
        }
    }

    companion object {

        private val ENTITY_DIFF_CALLBACK = object : DiffUtil.ItemCallback<IEntity>() {
            override fun areItemsTheSame(oldItem: IEntity, newItem: IEntity): Boolean {
                return if (oldItem is Article && newItem is Article) {
                    oldItem.id == newItem.id
                } else {
                    false
                }
            }

            override fun areContentsTheSame(oldItem: IEntity, newItem: IEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    /**
     * use sealed class to define view type
     */
    sealed class IViewType(val type: Int) {
        class ArticleViewType() : IViewType(1) {}
        class SeparatorViewType() : IViewType(-1) {}
    }
}