package com.darcy.message.lib_ui.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.darcy.message.lib_ui.databinding.LibUiItemArticleBinding
import com.darcy.message.lib_ui.paging.adapter.viewholder.ArticleViewHolder
import com.darcy.message.lib_ui.paging.entity.IEntity.Article

/**
 * adapter for article
 */
class ArticleAdapter : PagingDataAdapter<Article, ArticleViewHolder>(ARTICLE_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(
            LibUiItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val tile = getItem(position)
        if (tile != null) {
            holder.bind(tile)
        }
    }

    companion object {
        private val ARTICLE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }
}