package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.Article
import com.raassh.gemastik15.databinding.ArticleItemBinding
import com.raassh.gemastik15.utils.getElapsedTime
import com.raassh.gemastik15.utils.loadImage
import com.raassh.gemastik15.utils.translateArticleTypeFromDB

class ArticleAdapter : ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(DIFF_CALLBACK) {
    var onItemClickListener: ((Article) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArticleViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
    )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ArticleItemBinding.bind(itemView)
        private val context = itemView.context

        init {
            binding.apply {
                root.setOnClickListener {
                    onItemClickListener?.invoke(getItem(adapterPosition))
                }
            }
        }

        fun bind(article: Article) {
            binding.apply {
                imgArticleImage.loadImage(article.imageUrl)
                tvArticleType.text = context.translateArticleTypeFromDB(article.type)
                tvArticleTitle.text = article.title
                tvArticleDate.text = context.getElapsedTime(article.publishTime)
                tvArticleSource.text = article.source
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}